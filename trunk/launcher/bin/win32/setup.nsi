!include MUI2.nsh
!include ZipDLL.nsh

name "${chenance.name}"

# define the name of the installer
outfile "chenance-${chenance.version.short}-setup.exe"

# define the directory to install to, the desktop in this case as specified  
# by the predefined $DESKTOP variable
installDir $DESKTOP

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
	readRegStr $0 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" CurrentVersion
 
	# print the results in a popup message box
	messageBox MB_OK "version: $0"

	call GetLib
	call GetSwt

	# define what to install and place it in the output path
	file chenance.exe
	file chenance-gui-${chenance.version.short}.jar
	file chenance-data-${chenance.data.version}.jar

sectionEnd

!define LIB_URL http://chenance.googlecode.com/files/lib.zip
!define SWT_URL http://ftp.jaist.ac.jp/pub/eclipse/eclipse/downloads/drops/S-3.4RC3-200805301730/swt-3.4RC3-win32-win32-x86.zip

Function GetLib
    StrCpy $2 "$TEMP\lib.zip"
    MessageBox MB_OK "$2"
        ;nsisdl::download /TIMEOUT=30000 ${LIB_URL} $2
        ;Pop $R0 ;Get the return value
        ;        StrCmp $R0 "success" +3
        ;        MessageBox MB_OK "Download failed: $R0"
        ;        Quit
    ZipDLL::extractall $2 "$INSTDIR\lib"
    Delete $2
FunctionEnd

Function GetSwt
    StrCpy $2 "$TEMP\swt.zip"
    MessageBox MB_OK "$2"
        ;nsisdl::download /TIMEOUT=30000 ${SWT_URL} $2
        ;Pop $R0 ;Get the return value
        ;        StrCmp $R0 "success" +3
        ;        MessageBox MB_OK "Download failed: $R0"
        ;        Quit
    ZipDLL::extractfile $2 "$INSTDIR\lib" "swt.jar"
    Delete $2
FunctionEnd

Function GetJRE
        MessageBox MB_OK "${PRODUCT_NAME} uses Java ${JRE_VERSION}, it will now \
                         be downloaded and installed"
 
        StrCpy $2 "$TEMP\Java Runtime Environment.exe"
        nsisdl::download /TIMEOUT=30000 ${JRE_URL} $2
        Pop $R0 ;Get the return value
                StrCmp $R0 "success" +3
                MessageBox MB_OK "Download failed: $R0"
                Quit
        ExecWait $2
        Delete $2
FunctionEnd