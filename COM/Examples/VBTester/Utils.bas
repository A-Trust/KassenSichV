Attribute VB_Name = "Utils"
Private Declare Function GetTempFileName Lib "Kernel32" Alias _
    "GetTempFileNameA" (ByVal lpszPath As String, _
    ByVal lpPrefixString As String, ByVal wUnique As Long, _
    ByVal lpTempFileName As String) As Long
Private Declare Function GetTempPath Lib "Kernel32" Alias "GetTempPathA" (ByVal _
    nBufferLength As Long, ByVal lpBuffer As String) As Long

' Creates a temporary (0 byte) file in the \TEMP directory
' and returns its name

Public Function GetTempFile(Optional Prefix As String) As String
    Dim TempFile As String
    Dim TempPath As String
    Const MAX_PATH = 260
    
    ' get the path of the \TEMP directory
    TempPath = Space$(MAX_PATH)
    GetTempPath Len(TempPath), TempPath
    ' trim off characters in excess
    TempPath = Left$(TempPath, InStr(TempPath & vbNullChar, vbNullChar) - 1)
    
    ' get the name of a temporary file in that path, with a given prefix
    TempFile = Space$(MAX_PATH)
    GetTempFileName TempPath, Prefix, 0, TempFile
    GetTempFile = Left$(TempFile, InStr(TempFile & vbNullChar, vbNullChar) - 1)

End Function

