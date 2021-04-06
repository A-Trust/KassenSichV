VERSION 5.00
Begin VB.Form Form1 
   Caption         =   "A-Sign TSE Tester"
   ClientHeight    =   10650
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   20100
   LinkTopic       =   "Form1"
   ScaleHeight     =   10650
   ScaleWidth      =   20100
   StartUpPosition =   3  'Windows Default
   Begin VB.TextBox Text_TransNum 
      Height          =   285
      Left            =   9000
      TabIndex        =   74
      Top             =   6120
      Width           =   1215
   End
   Begin VB.TextBox Text_ClientId 
      Height          =   285
      Left            =   7800
      TabIndex        =   69
      Text            =   "kassa1"
      Top             =   3240
      Width           =   1575
   End
   Begin VB.Frame Frame2 
      Caption         =   "Management Functions"
      Height          =   2775
      Left            =   120
      TabIndex        =   52
      Top             =   120
      Width           =   11295
      Begin VB.CommandButton Command_Version 
         Caption         =   "Get Version"
         Height          =   255
         Left            =   120
         TabIndex        =   76
         Top             =   360
         Width           =   1455
      End
      Begin VB.CommandButton Command_InitTse 
         Caption         =   "Initialize TSE"
         Height          =   255
         Left            =   4320
         TabIndex        =   75
         Top             =   1800
         Width           =   1335
      End
      Begin VB.ComboBox Combo_User 
         Height          =   315
         Left            =   9360
         Style           =   2  'Dropdown List
         TabIndex        =   72
         Top             =   1560
         Width           =   1455
      End
      Begin VB.TextBox Text_Unbl_newpin 
         Height          =   285
         Left            =   9360
         MaxLength       =   8
         TabIndex        =   71
         Top             =   2280
         Width           =   1455
      End
      Begin VB.TextBox Text_Unbl_Puk 
         Height          =   285
         Left            =   9360
         TabIndex        =   70
         Top             =   1920
         Width           =   1455
      End
      Begin VB.CommandButton Command_Logout 
         Caption         =   "Logout"
         Height          =   255
         Left            =   4320
         TabIndex        =   66
         Top             =   1200
         Width           =   1335
      End
      Begin VB.CommandButton Command_Unblock 
         Caption         =   "Unblock User"
         Height          =   255
         Left            =   8400
         TabIndex        =   63
         Top             =   1200
         Width           =   1575
      End
      Begin VB.TextBox Text_client_reg 
         Height          =   285
         Left            =   9360
         TabIndex        =   62
         Text            =   "kassa1"
         Top             =   720
         Width           =   1215
      End
      Begin VB.CommandButton Command_RegClient 
         Caption         =   "Register ClientId"
         Height          =   255
         Left            =   8400
         TabIndex        =   60
         Top             =   360
         Width           =   1575
      End
      Begin VB.TextBox Text_Auth_AdminPin 
         Height          =   285
         Left            =   5520
         MaxLength       =   8
         TabIndex        =   55
         Text            =   "11111111"
         Top             =   720
         Width           =   1215
      End
      Begin VB.CommandButton Command_AuthUser 
         Caption         =   "AuthenticateUser"
         Height          =   255
         Left            =   4320
         TabIndex        =   54
         Top             =   360
         Width           =   1575
      End
      Begin VB.CommandButton Command9 
         Caption         =   "Get Lifecycle State"
         Height          =   255
         Left            =   120
         TabIndex        =   53
         Top             =   840
         Width           =   1575
      End
      Begin VB.Label Label_Version 
         Caption         =   "xxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
         Height          =   375
         Left            =   1800
         TabIndex        =   77
         Top             =   360
         Width           =   2175
      End
      Begin VB.Label Label27 
         Caption         =   "New PIN"
         Height          =   375
         Left            =   8520
         TabIndex        =   67
         Top             =   2280
         Width           =   735
      End
      Begin VB.Label Label26 
         Caption         =   "PUK"
         Height          =   255
         Left            =   8520
         TabIndex        =   65
         Top             =   1920
         Width           =   615
      End
      Begin VB.Label Label25 
         Caption         =   "User"
         Height          =   255
         Left            =   8520
         TabIndex        =   64
         Top             =   1560
         Width           =   615
      End
      Begin VB.Label Label24 
         Caption         =   "Client Id"
         Height          =   255
         Left            =   8520
         TabIndex        =   61
         Top             =   720
         Width           =   1095
      End
      Begin VB.Label Label_AuthResult 
         Caption         =   "xxxxxxxxxxxxxxxxxxxxxxxxxxx"
         Height          =   255
         Left            =   6000
         TabIndex        =   59
         Top             =   360
         Width           =   2055
      End
      Begin VB.Label Label_LCS 
         Caption         =   "xxxxxxxxxxxxxxxxxxxxxxxxxxx"
         Height          =   255
         Left            =   1800
         TabIndex        =   58
         Top             =   840
         Width           =   2055
      End
      Begin VB.Label Label22 
         Caption         =   "Admin PIN"
         Height          =   255
         Left            =   4680
         TabIndex        =   57
         Top             =   720
         Width           =   975
      End
      Begin VB.Label Label18 
         Caption         =   "Label18"
         Height          =   15
         Left            =   2520
         TabIndex        =   56
         Top             =   1080
         Width           =   855
      End
   End
   Begin VB.Frame Frame1 
      Caption         =   " Einheit Only "
      Height          =   2775
      Left            =   11520
      TabIndex        =   42
      Top             =   120
      Width           =   8415
      Begin VB.CommandButton Command_PinStatus 
         Caption         =   "Get PIN Status"
         Height          =   255
         Left            =   240
         TabIndex        =   78
         Top             =   1320
         Width           =   1455
      End
      Begin VB.CommandButton Command8 
         Caption         =   "Set PINs/PUKs"
         Height          =   255
         Left            =   240
         TabIndex        =   51
         Top             =   360
         Width           =   1575
      End
      Begin VB.TextBox Text_SetTimeAdminPuk 
         Height          =   285
         Left            =   6360
         MaxLength       =   10
         TabIndex        =   50
         Text            =   "something!"
         Top             =   720
         Width           =   1095
      End
      Begin VB.TextBox Text_SetAdminPuk 
         Height          =   285
         Left            =   6360
         MaxLength       =   10
         TabIndex        =   49
         Text            =   "something!"
         Top             =   360
         Width           =   1095
      End
      Begin VB.TextBox Text_SetTimeAdminPin 
         Height          =   285
         Left            =   3240
         MaxLength       =   8
         TabIndex        =   46
         Text            =   "22222222"
         Top             =   720
         Width           =   1335
      End
      Begin VB.TextBox Text_SetAdminPin 
         Height          =   285
         Left            =   3240
         MaxLength       =   8
         TabIndex        =   45
         Text            =   "11111111"
         Top             =   360
         Width           =   1335
      End
      Begin VB.Label Label_PinStatus 
         Caption         =   "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
         Height          =   255
         Left            =   2040
         TabIndex        =   79
         Top             =   1320
         Width           =   2535
      End
      Begin VB.Label Label21 
         Caption         =   "Time Admin PUK"
         Height          =   255
         Left            =   4920
         TabIndex        =   48
         Top             =   720
         Width           =   1335
      End
      Begin VB.Label Label20 
         Caption         =   "Admin PUK"
         Height          =   375
         Left            =   4920
         TabIndex        =   47
         Top             =   360
         Width           =   975
      End
      Begin VB.Label Label19 
         Caption         =   "Time Admin PIN"
         Height          =   255
         Left            =   2040
         TabIndex        =   44
         Top             =   720
         Width           =   1215
      End
      Begin VB.Label Label17 
         Caption         =   "Admin PIN"
         Height          =   255
         Left            =   2040
         TabIndex        =   43
         Top             =   360
         Width           =   855
      End
   End
   Begin VB.TextBox Text12 
      Height          =   375
      Left            =   3720
      TabIndex        =   41
      Top             =   9720
      Width           =   8295
   End
   Begin VB.CommandButton Command6 
      Caption         =   "Get PublicKey"
      Height          =   615
      Left            =   120
      TabIndex        =   40
      Top             =   9720
      Width           =   3015
   End
   Begin VB.CommandButton Command7 
      Caption         =   "FinishTransaction - Date As String"
      Height          =   615
      Left            =   3720
      TabIndex        =   39
      Top             =   6120
      Width           =   3495
   End
   Begin VB.CommandButton Command5 
      Caption         =   "Start Transaction - Date As String"
      Height          =   495
      Left            =   3120
      TabIndex        =   38
      Top             =   3120
      Width           =   3255
   End
   Begin VB.TextBox TextMargin 
      Height          =   375
      Left            =   14760
      TabIndex        =   33
      Text            =   "3"
      Top             =   3720
      Width           =   1335
   End
   Begin VB.ComboBox Combo1 
      Height          =   315
      ItemData        =   "MainForm.frx":0000
      Left            =   12600
      List            =   "MainForm.frx":0007
      Style           =   2  'Dropdown List
      TabIndex        =   32
      Top             =   3720
      Width           =   1095
   End
   Begin VB.ComboBox Combo2 
      Height          =   315
      Left            =   12960
      TabIndex        =   31
      Text            =   "Combo2"
      Top             =   4320
      Width           =   1575
   End
   Begin VB.ComboBox Combo3 
      Height          =   315
      Left            =   16440
      TabIndex        =   30
      Text            =   "Combo3"
      Top             =   4320
      Width           =   1455
   End
   Begin VB.CommandButton Command4 
      Caption         =   "Build QR-Code"
      Height          =   375
      Left            =   11640
      TabIndex        =   29
      Top             =   3240
      Width           =   4335
   End
   Begin VB.TextBox QrInput 
      Height          =   375
      Left            =   13200
      TabIndex        =   28
      Text            =   "TEST-QRCODE-DATA-TEST-QRCODE-DATA"
      Top             =   4920
      Width           =   3975
   End
   Begin VB.TextBox Text13 
      Height          =   285
      Left            =   5280
      TabIndex        =   26
      Text            =   "export.tar"
      Top             =   8640
      Width           =   3855
   End
   Begin VB.TextBox Text2 
      Height          =   285
      Left            =   1920
      TabIndex        =   24
      Top             =   9240
      Width           =   5175
   End
   Begin VB.CommandButton Command3 
      Caption         =   "ExportData"
      Height          =   615
      Left            =   120
      TabIndex        =   22
      Top             =   8520
      Width           =   3135
   End
   Begin VB.TextBox Text11 
      Appearance      =   0  'Flat
      BackColor       =   &H80000004&
      BorderStyle     =   0  'None
      Height          =   285
      Left            =   1440
      Locked          =   -1  'True
      TabIndex        =   21
      Top             =   8040
      Width           =   5295
   End
   Begin VB.TextBox Text10 
      Appearance      =   0  'Flat
      BackColor       =   &H80000004&
      BorderStyle     =   0  'None
      Height          =   375
      Left            =   1440
      Locked          =   -1  'True
      TabIndex        =   20
      Top             =   7680
      Width           =   5415
   End
   Begin VB.TextBox Text9 
      Appearance      =   0  'Flat
      BackColor       =   &H80000004&
      BorderStyle     =   0  'None
      Height          =   285
      Left            =   1440
      Locked          =   -1  'True
      TabIndex        =   19
      Top             =   7320
      Width           =   9975
   End
   Begin VB.TextBox Text8 
      Appearance      =   0  'Flat
      BackColor       =   &H80000004&
      BorderStyle     =   0  'None
      Height          =   285
      Left            =   1440
      Locked          =   -1  'True
      TabIndex        =   15
      Top             =   6960
      Width           =   5415
   End
   Begin VB.CommandButton Command2 
      Caption         =   "FinishTransaction"
      Height          =   615
      Left            =   120
      TabIndex        =   13
      Top             =   6120
      Width           =   3375
   End
   Begin VB.TextBox Text7 
      Appearance      =   0  'Flat
      BackColor       =   &H80000004&
      BorderStyle     =   0  'None
      Height          =   285
      Left            =   1440
      Locked          =   -1  'True
      TabIndex        =   12
      Top             =   3720
      Width           =   7935
   End
   Begin VB.TextBox Text6 
      Appearance      =   0  'Flat
      BackColor       =   &H80000004&
      BorderStyle     =   0  'None
      Height          =   285
      Left            =   1440
      Locked          =   -1  'True
      TabIndex        =   10
      Top             =   5520
      Width           =   7935
   End
   Begin VB.TextBox Text5 
      Appearance      =   0  'Flat
      BackColor       =   &H80000004&
      BorderStyle     =   0  'None
      Height          =   285
      Left            =   1440
      Locked          =   -1  'True
      TabIndex        =   8
      Top             =   5160
      Width           =   7935
   End
   Begin VB.TextBox Text4 
      Appearance      =   0  'Flat
      BackColor       =   &H80000004&
      BorderStyle     =   0  'None
      Height          =   285
      Left            =   1440
      Locked          =   -1  'True
      TabIndex        =   6
      Top             =   4800
      Width           =   7935
   End
   Begin VB.TextBox Text3 
      Appearance      =   0  'Flat
      BackColor       =   &H80000004&
      BorderStyle     =   0  'None
      Height          =   285
      Left            =   1440
      Locked          =   -1  'True
      TabIndex        =   4
      Top             =   4440
      Width           =   9855
   End
   Begin VB.TextBox Text1 
      Appearance      =   0  'Flat
      BackColor       =   &H80000004&
      BorderStyle     =   0  'None
      Height          =   285
      Left            =   1440
      Locked          =   -1  'True
      TabIndex        =   1
      Top             =   4080
      Width           =   7935
   End
   Begin VB.CommandButton Command1 
      Caption         =   "Start Transaction"
      Height          =   495
      Left            =   120
      TabIndex        =   0
      Top             =   3120
      Width           =   2775
   End
   Begin VB.Label Label23 
      Caption         =   "Transaction Number"
      Height          =   495
      Left            =   7560
      TabIndex        =   73
      Top             =   6120
      Width           =   1335
   End
   Begin VB.Label Label28 
      Caption         =   "Client Id"
      Height          =   255
      Left            =   6840
      TabIndex        =   68
      Top             =   3240
      Width           =   855
   End
   Begin VB.Label Label16 
      Caption         =   "Margin:"
      Height          =   375
      Left            =   14040
      TabIndex        =   37
      Top             =   3720
      Width           =   735
   End
   Begin VB.Label Label15 
      Caption         =   "Scaling:"
      Height          =   255
      Left            =   11640
      TabIndex        =   36
      Top             =   3720
      Width           =   855
   End
   Begin VB.Label Label1 
      Caption         =   "Colour depth (bit):"
      Height          =   375
      Index           =   0
      Left            =   11640
      TabIndex        =   35
      Top             =   4320
      Width           =   1335
   End
   Begin VB.Label Label14 
      Caption         =   "Error correction level"
      Height          =   375
      Left            =   14760
      TabIndex        =   34
      Top             =   4320
      Width           =   1815
   End
   Begin VB.Label Label12 
      Caption         =   "QR-Code Data:"
      Height          =   255
      Left            =   11640
      TabIndex        =   27
      Top             =   4920
      Width           =   1335
   End
   Begin VB.Image Image1 
      Height          =   4215
      Left            =   12960
      Top             =   5520
      Width           =   4815
   End
   Begin VB.Label Label13 
      Caption         =   "File Output"
      Height          =   375
      Left            =   3960
      TabIndex        =   25
      Top             =   8640
      Width           =   1215
   End
   Begin VB.Label Label2 
      Caption         =   "ret code"
      Height          =   375
      Left            =   240
      TabIndex        =   23
      Top             =   9240
      Width           =   1455
   End
   Begin VB.Label Label11 
      Caption         =   "sig counter"
      Height          =   255
      Left            =   120
      TabIndex        =   18
      Top             =   8040
      Width           =   1095
   End
   Begin VB.Label Label10 
      Caption         =   "logtime"
      Height          =   375
      Left            =   120
      TabIndex        =   17
      Top             =   7680
      Width           =   975
   End
   Begin VB.Label Label9 
      Caption         =   "signature"
      Height          =   255
      Left            =   120
      TabIndex        =   16
      Top             =   7320
      Width           =   1335
   End
   Begin VB.Label Label8 
      Caption         =   "ret code"
      Height          =   255
      Left            =   120
      TabIndex        =   14
      Top             =   6960
      Width           =   1335
   End
   Begin VB.Label Label7 
      Caption         =   "res code"
      Height          =   255
      Left            =   120
      TabIndex        =   11
      Top             =   3720
      Width           =   1095
   End
   Begin VB.Label Label6 
      Caption         =   "sig counter"
      Height          =   255
      Left            =   120
      TabIndex        =   9
      Top             =   5520
      Width           =   1095
   End
   Begin VB.Label Label5 
      Caption         =   "transaction num"
      Height          =   255
      Left            =   120
      TabIndex        =   7
      Top             =   5160
      Width           =   1215
   End
   Begin VB.Label Label4 
      Caption         =   "logtime"
      Height          =   255
      Left            =   120
      TabIndex        =   5
      Top             =   4800
      Width           =   1095
   End
   Begin VB.Label Label3 
      Caption         =   "signature value"
      Height          =   255
      Left            =   120
      TabIndex        =   3
      Top             =   4440
      Width           =   1095
   End
   Begin VB.Label Label1 
      Caption         =   "Serialnumber"
      Height          =   255
      Index           =   1
      Left            =   120
      TabIndex        =   2
      Top             =   4080
      Width           =   975
   End
End
Attribute VB_Name = "Form1"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Option Explicit
Dim comobj As Object
Dim comobjBase64 As Object
Dim comQr As Object


Dim clientId As String
Dim vtseId As String



Private Sub Command_AuthUser_Click()
'AuthenticateUser
    Dim result As Long
    Dim authResult As Long
    Dim authRetries As Long
    Dim authRetCode As Long
    Dim ok As Long
        
    result = comobj.AuthenticateUser("Admin", StrConv(Text_Auth_AdminPin.Text, vbFromUnicode), authResult, authRetries)
    
    If result <> 0 Then
        ok = MsgBox("Failed with return code " + RetCodeToString(result), vbYes, "Error", vbNull, 100)
        Label_AuthResult.Caption = ""
        Exit Sub
    End If
    
    If authResult = 0 Then
        Label_AuthResult.Caption = "Logged In"
    Else
        Label_AuthResult.Caption = "Login failed.  Retries: " + CStr(authRetries)
    End If


End Sub

Private Sub Command_InitTse_Click()
'InitializeDescriptionSet
    Dim result As Long
    Dim ok As Long
        
    result = comobj.InitializeDescriptionSet()
    
    If result = 0 Then
        ok = MsgBox("Initialized", vbYes, "OK", vbNull, 100)
        Label_LCS.Caption = ""
    Else
        ok = MsgBox("Failed with return code " + RetCodeToString(result), vbYes, "Error", vbNull, 100)
    End If

End Sub

Private Sub Command_Logout_Click()
'Logout
    Dim result As Long
    Dim ok As Long
        
    result = comobj.LogOut("Admin")
    
    If result <> 0 Then
        ok = MsgBox("Failed with return code " + RetCodeToString(result), vbYes, "Error", vbNull, 100)
        Label_AuthResult.Caption = ""
        Exit Sub
    Else
        Label_AuthResult.Caption = "Logged Out"
    End If
    
End Sub

Private Sub Command_PinStatus_Click()
'Get PIN Status
    Dim result As Long
    Dim pinStatus As Long
    Dim ok As Long
    result = comobj.CvGetPinStatus(pinStatus)
    
    Label_PinStatus.Caption = ""
     
    If result <> 0 Then
        ok = MsgBox("Failed with return code " + RetCodeToString(result), vbYes, "Error", vbNull, 100)
        Exit Sub
    End If
    
    If pinStatus = 0 Then
        Label_PinStatus.Caption = "PINs initialized"
    Else
        Label_PinStatus.Caption = "PINs not initialized"
    End If

End Sub

Private Sub Command_RegClient_Click()
'Register client id
    Dim result As Long
    Dim ok As Long
    result = comobj.AtRegisterClientId(Text_client_reg.Text)
    
    If result = 0 Then
        ok = MsgBox("Registered client id", vbYes, "OK", vbNull, 100)
        Text_ClientId.Text = Text_client_reg.Text
    Else
        ok = MsgBox("Failed with return code " + RetCodeToString(result), vbYes, "Error", vbNull, 100)
    End If
    
End Sub

Private Sub Command_Unblock_Click()
'Unblock user
    Dim result As Long
    Dim ok As Long
    Dim unblock_res As Long
    Dim ret As Long
    result = comobj.UnblockUser(Combo_User.Text, StrConv(Text_Unbl_Puk.Text, vbFromUnicode), StrConv(Text_Unbl_newpin.Text, vbFromUnicode), unblock_res)
    
    If result = 0 And unblock_res = 0 Then
        ok = MsgBox("User unblocked", vbYes, "OK", vbNull, 100)
    Else
        ok = MsgBox("Failed with return code " + RetCodeToString(result) + ", unblock result: " + CStr(unblock_res), vbYes, "Error", vbNull, 100)
    End If


End Sub

Private Sub Command_Version_Click()
'Get Version
    Dim result As Long
    Dim version As String
    result = comobj.AtGetVersion(version)
    
    If result = 0 Then
        Label_Version.Caption = version
    Else
        Label_Version.Caption = RetCodeToString(result)
    End If
    
End Sub

Private Sub Command3_Click()
    Dim maximumNumberRecords As Long: maximumNumberRecords = 0
    Dim exportedData() As Byte
    Dim result As Long
    Dim temp As String
    result = comobj.ExportData(maximumNumberRecords, exportedData)
    
    Text2.Text = result
    
    Dim f As Long
    f = FreeFile()
    Open Text13.Text For Binary As #f
    Put #f, , exportedData
    Close #f
    
    result = MsgBox("file written", vbYes, "OK", vbNull, 100)
End Sub

Private Sub Command4_Click()
    ''qrencode
    Dim sf As Long: sf = CLng(Combo1.Text)
    Dim b As Long: b = CLng(Combo2.Text)
    Dim m As Long: m = CLng(TextMargin.Text)
    Dim indata As String: indata = QrInput.Text
    Dim errorCorrection As String: errorCorrection = Combo3.Text
    
    Dim file As String: file = Utils.GetTempFile("qrfile") & ".bmp"
    
    comQr.SetMargin (m)
    comQr.SetScaleFactor (sf)
    comQr.SetBitDepth (b)
    comQr.SetErrorCorrectionLevel (errorCorrection)
    Dim res As Long: res = comQr.EncodeToFile(indata, file)
    
    Set Image1.Picture = LoadPicture(file)
End Sub

Private Sub Command5_Click()
    Dim proccessData As String: proccessData = ""
    Dim processType As String: processType = ""
    Dim additionalData As String: additionalData = ""
    Dim transactionNumber As Long
    Dim logTime As String: logTime = ""
    Dim serialNumber() As Byte
    Dim signatureCounter As Long
    Dim signatureValue() As Byte
    Dim temp As String
    
    Dim result As Long
    ' result = comobj.StartTransactionWithTse_str(clientId, proccessData, processType, additionalData, transactionNumber, logTime, serialNumber, signatureCounter, signatureValue, vtseId)
    result = comobj.StartTransaction_str(clientId, proccessData, processType, additionalData, transactionNumber, logTime, serialNumber, signatureCounter, signatureValue)
    
    Text7.Text = RetCodeToString(result)
    
    result = comobjBase64.Encode(serialNumber, temp)
    Text1.Text = temp
    
    result = comobjBase64.Encode(signatureValue, temp)
    Text3.Text = temp
    Text4.Text = logTime
    Text5.Text = transactionNumber
    Text_TransNum.Text = Text5.Text
    Text6.Text = signatureCounter
    
End Sub

Private Sub Command6_Click()
    Dim result As Long
    Dim publicKey() As Byte
    Dim temp As String

    result = comobj.AtGetPublicKey(publicKey)
    
    result = comobjBase64.Encode(publicKey, temp)
    Text12.Text = temp
End Sub

Private Sub Command7_Click()
    Dim result As Long
    Dim proccessData As String: proccessData = "Beleg^75.33_7.99_0.00_0.00_0.00^10.00:Bar_5.00 : Bar : CHF_5.00 : Bar : USD_64.30 : Unbar"
    Dim processType As String: processType = "Kassenbeleg-V1"
    Dim additionalData As String: additionalData = ""
    Dim logTime As String: logTime = ""
    Dim signatureValue() As Byte
    Dim transactionNumber As Long: transactionNumber = Int(Text_TransNum.Text)
    Dim temp As String
    Dim signatureCounter As Long
    
    'result = comobj.FinishTransactionWithTse_str(clientId, transactionNumber, proccessData, processType, additionalData, logTime, signatureCounter, signatureValue, vtseId)
    result = comobj.FinishTransaction_str(clientId, transactionNumber, proccessData, processType, additionalData, logTime, signatureCounter, signatureValue)
    Text8.Text = result
    
    result = comobjBase64.Encode(signatureValue, temp)
    Text9.Text = temp
    Text10.Text = logTime
    Text11.Text = signatureCounter
    
End Sub

Private Sub Command8_Click()
'InitializePinValues
    Dim result As Long
    Dim ok As Long
    result = comobj.CvInitializePinValues(StrConv(Text_SetAdminPin.Text, vbFromUnicode), StrConv(Text_SetAdminPuk.Text, vbFromUnicode), StrConv(Text_SetTimeAdminPin.Text, vbFromUnicode), StrConv(Text_SetTimeAdminPuk.Text, vbFromUnicode))
    
    If result = 0 Then
        ok = MsgBox("Set PINs/PUKs", vbYes, "OK", vbNull, 100)
        Label_PinStatus = ""
    Else
        ok = MsgBox("Failed with return code " + RetCodeToString(result), vbYes, "Error", vbNull, 100)
    End If
    
End Sub

Private Sub Command9_Click()
'Get Lifecycle State
    Dim result As Long
    Dim lcs As Long
    Dim ok As Long
    result = comobj.AtGetLifeCycleState(lcs)
    
    If result <> 0 Then
        ok = MsgBox("failed", vbYes, "OK", vbNull, 100)
        Label_LCS.Caption = ""
        Exit Sub
    End If
        
    If lcs = 1 Then
        Label_LCS.Caption = "NotInitialized"
    ElseIf lcs = 2 Then
        Label_LCS.Caption = "Active"
    ElseIf lcs = 3 Then
        Label_LCS.Caption = "Suspended"
    ElseIf lcs = 4 Then
        Label_LCS.Caption = "Disabled"
    Else
        Label_LCS.Caption = "Unknown"
    End If
    


End Sub

Private Sub Form_Load()
    Set comobj = CreateObject("asigntsecom.tse")
    Set comobjBase64 = CreateObject("asigntsecom.Base64")
    Set comQr = CreateObject("asigntsecom.QrCode")
    Dim result As Long
    
    Dim logTimeFormat As String
    Dim publicKey() As Byte
    
    clientId = "kassa1"
    vtseId = "default"
    
    Label_AuthResult.Caption = "Logged Out"
    Label_LCS.Caption = ""
    Label_Version.Caption = ""
    Label_PinStatus = ""
    
    Combo_User.Clear
    Combo_User.AddItem ("Admin")
    Combo_User.AddItem ("TimeAdmin")
    Combo_User.Text = "Admin"
    
    Combo1.Clear
    Combo1.AddItem "1"
    Combo1.AddItem "2"
    Combo1.AddItem "3"
    Combo1.AddItem "4"
    Combo1.AddItem "5"
    Combo1.AddItem "6"
    Combo1.Text = "2"
    
    
    Combo2.Clear
    Combo2.AddItem "1"
    Combo2.AddItem "4"
    Combo2.AddItem "8"
    Combo2.AddItem "16"
    Combo2.AddItem "24"
    Combo2.AddItem "32"
    Combo2.Text = "24"
    
    Combo3.Clear
    Combo3.AddItem "L"
    Combo3.AddItem "M"
    Combo3.AddItem "Q"
    Combo3.AddItem "H"
    Combo3.Text = "H"
    
    result = comobj.CfgSetLogDir(".")
    result = comobj.CfgSetLoggingEnabled(True)
    result = comobj.CfgSetLoggingFile(True)
    
    result = comobj.AtGetLogTimeFormat(logTimeFormat)
    
    result = comobj.AtLoad()


    
End Sub

Private Sub Command1_Click()
    Dim proccessData As String: proccessData = ""
    Dim processType As String: processType = ""
    Dim additionalData As String: additionalData = ""
    Dim transactionNumber As Long
    Dim logTime As Date
    Dim serialNumber() As Byte
    Dim signatureCounter As Long
    Dim signatureValue() As Byte
    Dim temp As String
    
    clientId = Text_ClientId.Text
    
    Dim result As Long
    ' result = comobj.StartTransactionWithTse(clientId, proccessData, processType, additionalData, transactionNumber, logTime, serialNumber, signatureCounter, signatureValue, vtseId)
    result = comobj.StartTransaction(clientId, proccessData, processType, additionalData, transactionNumber, logTime, serialNumber, signatureCounter, signatureValue)
    
    Text7.Text = RetCodeToString(result)
    
    result = comobjBase64.Encode(serialNumber, temp)
    Text1.Text = temp
    
    result = comobjBase64.Encode(signatureValue, temp)
    Text3.Text = temp
    Text4.Text = logTime
    Text5.Text = transactionNumber
    Text_TransNum.Text = Text5.Text

    Text6.Text = signatureCounter
    
End Sub


Private Sub Command2_Click()
    Dim result As Long
    Dim proccessData As String: proccessData = "Beleg^75.33_7.99_0.00_0.00_0.00^10.00:Bar_5.00 : Bar : CHF_5.00 : Bar : USD_64.30 : Unbar"
    Dim processType As String: processType = "Kassenbeleg-V1"
    Dim additionalData As String: additionalData = ""
    Dim logTime As Date
    Dim signatureValue() As Byte
    Dim transactionNumber As Long: transactionNumber = Int(Text_TransNum.Text)
    Dim temp As String
    Dim signatureCounter As Long
    
    clientId = Text_ClientId.Text
        
    'result = comobj.FinishTransactionWithTse(clientId, transactionNumber, proccessData, processType, additionalData, logTime, signatureCounter, signatureValue, vtseId)
    result = comobj.FinishTransaction(clientId, transactionNumber, proccessData, processType, additionalData, logTime, signatureCounter, signatureValue)
    Text8.Text = RetCodeToString(result)
    
    result = comobjBase64.Encode(signatureValue, temp)
    Text9.Text = temp
    Text10.Text = logTime
    Text11.Text = signatureCounter
End Sub


Function RetCodeToString(ret As Long) As String

    If ret = 0 Then
        RetCodeToString = "OK (0)"
    ElseIf ret = -5017 Then
        RetCodeToString = "ERROR_NO_TRANSACTION (-5017)"
    ElseIf ret = -5018 Then
        RetCodeToString = "ERROR_SE_API_NOT_INITIALIZED (-5018)"
    ElseIf ret = -5023 Then
        RetCodeToString = "ERROR_USER_NOT_AUTHENTICATED (-5023)"
    ElseIf ret = -3006 Then
        RetCodeToString = "ERROR_SE_COMMUNICATION_FAILED (-3006)"
    ElseIf ret = -3010 Then
        RetCodeToString = "ERROR_NO_ERS (-3010)"
    Else
        RetCodeToString = "Error " + CStr(ret)
    End If
    
    
    
End Function
