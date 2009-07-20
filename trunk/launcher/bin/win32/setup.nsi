!include MUI2.nsh
!include ZipDLL.nsh

name "${chenance.name}"

# define the name of the installer
outfile "chenance-${chenance.version.short}-setup.exe"

# define the directory to install to, the desktop in this case as specified  
# by the predefined $DESKTOP variable
installDir $PROGRAMFILES\Chenance

!define MUI_LICENSEPAGE_TEXT_TOP "License and Agreements"
!define MUI_LICENSEPAGE_TEXT_BOTTOM "Click 'I Agree' to forward"
!define MUI_LICENSEPAGE_CHECKBOX
!insertmacro MUI_PAGE_LICENSE "License.rtf"
!insertmacro MUI_PAGE_DIRECTORY
!insertmacro MUI_PAGE_INSTFILES

# default section
section
 
# define the output path for this file
setOutPath $INSTDIR

	# read the value from the registry into the $0 register
	;readRegStr $0 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" CurrentVersion
 
	# print the results in a popup message box
	;messageBox MB_OK "version: $0"
    
    call GetJRE
	call GetLib
	call GetSwt

	# define what to install and place it in the output path
	file chenance.exe
	file chenance-gui-${chenance.version.short}.jar
	file chenance-data-${chenance.data.version}.jar

sectionEnd

!define LIB_URL http://chenance.googlecode.com/files/lib.zip
!define SWT_URL http://www.eclipse.org/downloads/download.php?file=/eclipse/downloads/drops/R-3.5-200906111540/swt-3.5-win32-win32-x86.zip&url=http://download.eclipse.org/eclipse/downloads/drops/R-3.5-200906111540/swt-3.5-win32-win32-x86.zip&mirror_id=1
!define JRE_VERSION "5.0"
!define JRE_URL "http://javadl.sun.com/webapps/download/AutoDL?BundleId=22933&/jre-1_5_0_16-windows-i586-p.exe"
!define JAVAEXE "javaw.exe"

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
    Call CheckJREVersion
    IfErrors CheckRegistry JreFound
 
  ; 3) Check for registry
  CheckRegistry:
    ClearErrors
    ReadRegStr $R1 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
    ReadRegStr $R0 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$R1" "JavaHome"
    StrCpy $R0 "$R0\bin\${JAVAEXE}"
    IfErrors DownloadJRE
    IfFileExists $R0 0 DownloadJRE
    Call CheckJREVersion
    IfErrors DownloadJRE JreFound
 
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
 
    ReadRegStr $R1 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
    ReadRegStr $R0 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$R1" "JavaHome"
    StrCpy $R0 "$R0\bin\${JAVAEXE}"
    IfFileExists $R0 0 GoodLuck
    Call CheckJREVersion
    IfErrors GoodLuck JreFound
 
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
 
; Pass the "javaw.exe" path by $R0
Function CheckJREVersion
    Push $R1
 
    ; Get the file version of javaw.exe
    ${GetFileVersion} $R0 $R1
    ${VersionCompare} ${JRE_VERSION} $R1 $R1
 
    ; Check whether $R1 != "1"
    ClearErrors
    StrCmp $R1 "1" 0 CheckDone
    SetErrors
 
  CheckDone:
    Pop $R1
FunctionEnd

