;!include UAC.nsh
!include MUI2.nsh
!include ZipDLL.nsh

RequestExecutionLevel user

!define VERSION ${chenance.version.short}
!define GUI_JAR chenance-gui-${chenance.gui.version}.jar
!define DATA_JAR chenance-data-${chenance.data.version}.jar
# define the name of the installer
!define INST_FILE "chenance-${chenance.version.short}-setup.exe"
!define LIB_URL http://chenance.googlecode.com/files/lib.zip
;!define SWT_URL http://www.eclipse.org/downloads/download.php?file=/eclipse/downloads/drops/R-3.5-200906111540/swt-3.5-win32-win32-x86.zip&url=http://download.eclipse.org/eclipse/downloads/drops/R-3.5-200906111540/swt-3.5-win32-win32-x86.zip&mirror_id=1
!define SWT_URL http://ftp.jaist.ac.jp/pub/eclipse/eclipse/downloads/drops/R-3.5-200906111540/swt-3.5-win32-win32-x86.zip

!define JRE_VERSION "5.0"
!define JRE_URL "http://javadl.sun.com/webapps/download/AutoDL?BundleId=22933&/jre-1_5_0_16-windows-i586-p.exe"
!define JAVAEXE "javaw.exe"

name "${chenance.name}"
outfile ${INST_FILE}
installDir $PROGRAMFILES\Chenance

!define MUI_LICENSEPAGE_TEXT_TOP "License and Agreements"
!define MUI_LICENSEPAGE_TEXT_BOTTOM "Click 'I Agree' to forward"
!define MUI_LICENSEPAGE_CHECKBOX
!define LICENSE_FILE "License.rtf"
!insertmacro MUI_PAGE_LICENSE ${LICENSE_FILE}
!insertmacro MUI_PAGE_DIRECTORY
#!insertmacro MUI_PAGE_COMPONENTS
!insertmacro MUI_PAGE_INSTFILES

!define REG_UNINSTALL "Software\Microsoft\Windows\CurrentVersion\Uninstall\Chenance"

# default section
section
 
    ReadRegStr $R0 HKLM "$REG_UNINSTALL" "DisplayVersion"
    StrCmp $R0 "${VERSION}" UPGRADE
    
    # define the output path for this file
    setOutPath $INSTDIR

    # read the value from the registry into the $0 register
    ;readRegStr $0 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" CurrentVersion
 
    # print the results in a popup message box
    ;messageBox MB_OK "version: $0"
    
    call GetJRE
    IfFileExists "$INSTDIR\lib" +2
      call GetLib
    IfFileExists "$INSTDIR\lib\swt.jar" +2
      call GetSwt

    # define what to install and place it in the output path
    file chenance.exe
    file ${GUI_JAR}
    file ${DATA_JAR}
    ;file ${INST_FILE}

    createShortCut "$Desktop\Chenance.lnk" "$INSTDIR\chenance.exe"

    WriteUninstaller $INSTDIR\uninstall.exe
    WriteRegStr HKLM "${REG_UNINSTALL}" \
        "DisplayIcon" "$\"$INSTDIR\chenance.exe$\", 1"
    WriteRegStr HKLM "${REG_UNINSTALL}" \
        "DisplayName" "Chenance - a Personal Finance Manager"
    WriteRegStr HKLM "${REG_UNINSTALL}" \
         "Publisher" "http://code.google.com/p/chenance/"
    WriteRegStr HKLM "${REG_UNINSTALL}" \
        "DisplayVersion" "${VERSION}"
    WriteRegStr HKLM "${REG_UNINSTALL}" \
        "UninstallString" "$INSTDIR\uninstall.exe"
    WriteRegDWord HKLM "${REG_UNINSTALL}" \
        "NoModify" 0
    WriteRegDWord HKLM "${REG_UNINSTALL}" \
        "NoRepair" 0
    WriteRegStr HKLM "${REG_UNINSTALL}" \
        "ModifyPath" '"$INSTDIR\${INST_FILE}"'
    ;WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Chenance" \
    ;                 "QuietUninstallString" "$\"$INSTDIR\uninstall.exe$\" /S"

  UPGRADE:

sectionEnd

section "uninstall"
    delete "$INSTDIR\uninstall.exe"
delete "$DESKTOP\Chenance.lnk"
;Rmdir "$STARTMENU\Programs\myApp"
DeleteRegKey HKLM "${REG_UNINSTALL}"
sectionEnd

Function GetLib
    StrCpy $2 "$TEMP\lib.zip"
    #MessageBox MB_OK "$2"
    
    nsisdl::download /TIMEOUT=30000 ${LIB_URL} $2
    Pop $R0 ;Get the return value
    StrCmp $R0 "success" +3
        MessageBox MB_OK "Download failed: $R0"
        Quit
    
    ZipDLL::extractall $2 "$INSTDIR\lib"
    Delete $2
FunctionEnd

Function GetSwt
    StrCpy $2 "$TEMP\swt.zip"
    #MessageBox MB_OK "$2"
    
    nsisdl::download /TIMEOUT=30000 ${SWT_URL} $2
    Pop $R0 ;Get the return value
    StrCmp $R0 "success" +3
        MessageBox MB_OK "Download failed: $R0"
        Quit
    
    ZipDLL::extractfile $2 "$INSTDIR\lib" "swt.jar"
    Delete $2
FunctionEnd

;  returns the full path of a valid java.exe
;  looks in:
;  1 - .\jre directory (JRE Installed with application)
;  2 - JAVA_HOME environment variable
;  3 - the registry
;  4 - hopes it is in current dir or PATH
Function GetJRE
    Push $R0
    Push $R1
    Push $2
 
  ; 1) Check local JRE
  CheckLocal:
    ClearErrors
    StrCpy $R0 "$EXEDIR\jre\bin\${JAVAEXE}"
    IfFileExists $R0 JreFound
 
  ; 2) Check for JAVA_HOME
  CheckJavaHome:
    ClearErrors
    ReadEnvStr $R0 "JAVA_HOME"
    StrCpy $R0 "$R0\bin\${JAVAEXE}"
    IfErrors CheckRegistry     
    IfFileExists $R0 0 CheckRegistry
    ;Call CheckJREVersion
    ;IfErrors CheckRegistry JreFound
 
  ; 3) Check for registry
  CheckRegistry:
    ClearErrors
    ReadRegStr $R1 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
    ReadRegStr $R0 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$R1" "JavaHome"
    StrCpy $R0 "$R0\bin\${JAVAEXE}"
    IfErrors DownloadJRE JreFound
    IfFileExists $R0 JreFound DownloadJRE
    ;Call CheckJREVersion
    ;IfErrors DownloadJRE JreFound
 
  DownloadJRE:
    Call ElevateToAdmin
    MessageBox MB_ICONINFORMATION "${PRODUCT_NAME} uses Java Runtime Environment ${JRE_VERSION}, it will now be downloaded and installed."
    StrCpy $2 "$TEMP\Java Runtime Environment.exe"
    nsisdl::download /TIMEOUT=30000 ${JRE_URL} $2
    Pop $R0 ;Get the return value
    StrCmp $R0 "success" +3
      MessageBox MB_ICONSTOP "Download failed: $R0"
      Abort
    ExecWait $2
    Delete $2
 
    ;ReadRegStr $R1 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
    ;ReadRegStr $R0 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$R1" "JavaHome"
    ;StrCpy $R0 "$R0\bin\${JAVAEXE}"
    ;IfFileExists $R0 0 GoodLuck
    ;Call CheckJREVersion
    ;IfErrors GoodLuck JreFound
 
  ; 4) wishing you good luck
  GoodLuck:
    StrCpy $R0 "${JAVAEXE}"
    ; MessageBox MB_ICONSTOP "Cannot find appropriate Java Runtime Environment."
    ; Abort
 
  JreFound:
    Pop $2
    Pop $R1
    Exch $R0
FunctionEnd

; Attempt to give the UAC plug-in a user process and an admin process.
Function ElevateToAdmin
  UAC_Elevate:
    UAC::RunElevated
    StrCmp 1223 $0 UAC_ElevationAborted ; UAC dialog aborted by user?
    StrCmp 0 $0 0 UAC_Err ; Error?
    StrCmp 1 $1 0 UAC_Success ;Are we the real deal or just the wrapper?
    Quit
 
  UAC_ElevationAborted:
    # elevation was aborted, run as normal?
    MessageBox MB_ICONSTOP "This installer requires admin access, aborting!"
    Abort
 
  UAC_Err:
    MessageBox MB_ICONSTOP "Unable to elevate, error $0"
    Abort
 
  UAC_Success:
    StrCmp 1 $3 +4 ;Admin?
    StrCmp 3 $1 0 UAC_ElevationAborted ;Try again?
    MessageBox MB_ICONSTOP "This installer requires admin access, try again"
    goto UAC_Elevate 
FunctionEnd