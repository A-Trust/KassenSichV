namespace AsignTseExampleGUI
{
    partial class TseGUI
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(TseGUI));
            this.groupBox1 = new System.Windows.Forms.GroupBox();
            this.pictureBox = new System.Windows.Forms.PictureBox();
            this.label4 = new System.Windows.Forms.Label();
            this.button_genQR = new System.Windows.Forms.Button();
            this.textBox_processdata = new System.Windows.Forms.TextBox();
            this.textBox_type = new System.Windows.Forms.TextBox();
            this.label1 = new System.Windows.Forms.Label();
            this.comboBox_procType = new System.Windows.Forms.ComboBox();
            this.textBox_clientID = new System.Windows.Forms.TextBox();
            this.label2 = new System.Windows.Forms.Label();
            this.groupBox1.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.pictureBox)).BeginInit();
            this.SuspendLayout();
            // 
            // groupBox1
            // 
            this.groupBox1.Controls.Add(this.pictureBox);
            this.groupBox1.Controls.Add(this.label4);
            this.groupBox1.Controls.Add(this.button_genQR);
            this.groupBox1.Controls.Add(this.textBox_processdata);
            this.groupBox1.Controls.Add(this.textBox_type);
            this.groupBox1.Controls.Add(this.label1);
            this.groupBox1.Controls.Add(this.comboBox_procType);
            this.groupBox1.Controls.Add(this.textBox_clientID);
            this.groupBox1.Controls.Add(this.label2);
            this.groupBox1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.groupBox1.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.groupBox1.Location = new System.Drawing.Point(0, 0);
            this.groupBox1.Name = "groupBox1";
            this.groupBox1.Size = new System.Drawing.Size(634, 593);
            this.groupBox1.TabIndex = 1;
            this.groupBox1.TabStop = false;
            this.groupBox1.Text = "QR-Code";
            // 
            // pictureBox
            // 
            this.pictureBox.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.pictureBox.BackColor = System.Drawing.SystemColors.ActiveCaptionText;
            this.pictureBox.Location = new System.Drawing.Point(14, 98);
            this.pictureBox.Name = "pictureBox";
            this.pictureBox.Size = new System.Drawing.Size(613, 460);
            this.pictureBox.SizeMode = System.Windows.Forms.PictureBoxSizeMode.CenterImage;
            this.pictureBox.TabIndex = 1;
            this.pictureBox.TabStop = false;
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Location = new System.Drawing.Point(11, 75);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(87, 13);
            this.label4.TabIndex = 47;
            this.label4.Text = "ProcessData :";
            // 
            // button_genQR
            // 
            this.button_genQR.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
            this.button_genQR.Location = new System.Drawing.Point(500, 564);
            this.button_genQR.Name = "button_genQR";
            this.button_genQR.Size = new System.Drawing.Size(128, 23);
            this.button_genQR.TabIndex = 0;
            this.button_genQR.Text = "Generate QR-Code";
            this.button_genQR.UseVisualStyleBackColor = true;
            this.button_genQR.Click += new System.EventHandler(this.button_genQR_Click_1);
            // 
            // textBox_processdata
            // 
            this.textBox_processdata.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.textBox_processdata.Location = new System.Drawing.Point(105, 72);
            this.textBox_processdata.Name = "textBox_processdata";
            this.textBox_processdata.Size = new System.Drawing.Size(523, 20);
            this.textBox_processdata.TabIndex = 45;
            // 
            // textBox_type
            // 
            this.textBox_type.Location = new System.Drawing.Point(105, 45);
            this.textBox_type.Name = "textBox_type";
            this.textBox_type.Size = new System.Drawing.Size(203, 20);
            this.textBox_type.TabIndex = 44;
            this.textBox_type.Text = "Kassenbeleg-V1";
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(41, 22);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(58, 13);
            this.label1.TabIndex = 39;
            this.label1.Text = "ClientId :";
            // 
            // comboBox_procType
            // 
            this.comboBox_procType.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.comboBox_procType.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.comboBox_procType.FormattingEnabled = true;
            this.comboBox_procType.Items.AddRange(new object[] {
            "Kassenbeleg-V1",
            "Bestellung-V1"});
            this.comboBox_procType.Location = new System.Drawing.Point(522, 45);
            this.comboBox_procType.Name = "comboBox_procType";
            this.comboBox_procType.Size = new System.Drawing.Size(105, 21);
            this.comboBox_procType.TabIndex = 42;
            this.comboBox_procType.Visible = false;
            // 
            // textBox_clientID
            // 
            this.textBox_clientID.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.textBox_clientID.Location = new System.Drawing.Point(105, 19);
            this.textBox_clientID.Name = "textBox_clientID";
            this.textBox_clientID.Size = new System.Drawing.Size(209, 20);
            this.textBox_clientID.TabIndex = 38;
            this.textBox_clientID.Text = "955002-00";
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(11, 48);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(88, 13);
            this.label2.TabIndex = 41;
            this.label2.Text = "ProcessType :";
            // 
            // TseGUI
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(634, 593);
            this.Controls.Add(this.groupBox1);
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.Name = "TseGUI";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "ASignTseExampleGUI";
            this.groupBox1.ResumeLayout(false);
            this.groupBox1.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)(this.pictureBox)).EndInit();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.GroupBox groupBox1;
        private System.Windows.Forms.PictureBox pictureBox;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.Button button_genQR;
        private System.Windows.Forms.TextBox textBox_processdata;
        private System.Windows.Forms.TextBox textBox_type;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.ComboBox comboBox_procType;
        private System.Windows.Forms.TextBox textBox_clientID;
        private System.Windows.Forms.Label label2;
    }
}

