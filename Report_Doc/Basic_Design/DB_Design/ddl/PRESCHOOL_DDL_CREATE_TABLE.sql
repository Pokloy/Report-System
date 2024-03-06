-- User_Information
DROP TABLE M_USER_INFORMATION CASCADE;

CREATE TABLE M_USER_INFORMATION (
    id_pk serial NOT NULL 
   ,mail_address varchar(256)  NOT NULL 
   ,username varchar(50)  NOT NULL 
   ,role varchar(10)  DEFAULT 'USER'  NOT NULL 
   ,permission_id char(1)  DEFAULT '1'  NOT NULL 
   ,first_name varchar(50)  NOT NULL 
   ,last_name varchar(50)  NOT NULL 
   ,display_picture text NOT NULL 
   ,reg_id varchar(10)  NOT NULL 
   ,reg_date timestamp NOT NULL 
   ,update_id varchar(10)  NOT NULL 
   ,update_date timestamp NOT NULL 
   ,delete_flg boolean DEFAULT False  NOT NULL 
   ,PRIMARY KEY(id_pk)
);
ALTER TABLE M_USER_INFORMATION ADD CONSTRAINT m_user_information_mail_address_unique_key UNIQUE(mail_address);
ALTER TABLE M_USER_INFORMATION ADD CONSTRAINT m_user_information_username_unique_key UNIQUE(username);

-- User_Info_Account
DROP TABLE M_USER_INFO_ACCOUNT CASCADE;

CREATE TABLE M_USER_INFO_ACCOUNT (
    user_id_pk int NOT NULL 
   ,password varchar(255)  NOT NULL 
   ,reg_id varchar(10)  NOT NULL 
   ,reg_date timestamp NOT NULL 
   ,update_id varchar(10)  NOT NULL 
   ,update_date timestamp NOT NULL 
   ,delete_flg boolean DEFAULT False  NOT NULL 
   ,PRIMARY KEY(user_id_pk)
);
ALTER TABLE M_USER_INFO_ACCOUNT ADD FOREIGN KEY (user_id_pk) REFERENCES M_USER_INFORMATION(id_pk);

-- Group
DROP TABLE M_GROUP CASCADE;

CREATE TABLE M_GROUP (
    id_pk serial NOT NULL 
   ,group_name varchar(50)  NOT NULL 
   ,reg_id varchar(10)  NOT NULL 
   ,reg_date timestamp NOT NULL 
   ,update_id varchar(10)  NOT NULL 
   ,update_date timestamp NOT NULL 
   ,delete_flg boolean DEFAULT False  NOT NULL 
   ,PRIMARY KEY(id_pk)
);

-- Group_User_View
DROP TABLE T_GROUP_USER_VIEW CASCADE;

CREATE TABLE T_GROUP_USER_VIEW (
    group_id_pk int NOT NULL 
   ,user_id_pk int NOT NULL 
   ,reg_id varchar(10)  NOT NULL 
   ,reg_date timestamp NOT NULL 
   ,update_id varchar(10)  NOT NULL 
   ,update_date timestamp NOT NULL 
   ,delete_flg boolean DEFAULT False  NOT NULL 
   ,PRIMARY KEY(group_id_pk,user_id_pk)
);
ALTER TABLE T_GROUP_USER_VIEW ADD FOREIGN KEY (group_id_pk) REFERENCES M_GROUP(id_pk);
ALTER TABLE T_GROUP_USER_VIEW ADD FOREIGN KEY (user_id_pk) REFERENCES M_USER_INFORMATION(id_pk);

-- Daily_Report
DROP TABLE T_DAILY_REPORT CASCADE;

CREATE TABLE T_DAILY_REPORT (
    id_pk bigserial NOT NULL
   ,user_id_pk int NOT NULL 
   ,report_date char(8) NOT NULL
   ,target text NOT NULL 
   ,status char(1)  NOT NULL 
   ,reg_id varchar(10)  NOT NULL 
   ,reg_date timestamp NOT NULL 
   ,update_id varchar(10)  NOT NULL 
   ,update_date timestamp NOT NULL 
   ,delete_flg boolean DEFAULT False  NOT NULL 
   ,PRIMARY KEY(id_pk)
);

ALTER TABLE T_DAILY_REPORT ADD FOREIGN KEY (user_id_pk) REFERENCES M_USER_INFORMATION(id_pk);

-- Weekly_Pdf
DROP TABLE T_WEEKLY_PDF CASCADE;

CREATE TABLE T_WEEKLY_PDF (
    id_pk serial NOT NULL 
   ,user_id_pk int NOT NULL 
   ,file_path text NOT NULL 
   ,title varchar(255)  NOT NULL 
   ,reg_id varchar(10)  NOT NULL 
   ,reg_date timestamp NOT NULL 
   ,update_id varchar(10)  NOT NULL 
   ,update_date timestamp NOT NULL 
   ,delete_flg boolean DEFAULT False  NOT NULL 
   ,PRIMARY KEY(id_pk)
);
ALTER TABLE T_WEEKLY_PDF ADD FOREIGN KEY (user_id_pk) REFERENCES M_USER_INFORMATION(id_pk);

-- Self_Evaluation
DROP TABLE T_SELF_EVALUATION CASCADE;

CREATE TABLE T_SELF_EVALUATION (
    daily_report_id_pk int NOT NULL 
   ,comment text NOT NULL 
   ,rating int NOT NULL 
   ,reg_id varchar(10)  NOT NULL 
   ,reg_date timestamp NOT NULL 
   ,update_id varchar(10)  NOT NULL 
   ,update_date timestamp NOT NULL 
   ,delete_flg boolean DEFAULT False  NOT NULL 
   ,PRIMARY KEY(daily_report_id_pk)
);
ALTER TABLE T_SELF_EVALUATION ADD FOREIGN KEY (daily_report_id_pk) REFERENCES T_DAILY_REPORT(id_pk);

-- Final Evaluation
DROP TABLE T_FINAL_EVALUATION CASCADE;

CREATE TABLE T_FINAL_EVALUATION (
    daily_report_id_pk int NOT NULL 
   ,evaluator_id_pk int NOT NULL 
   ,comment text NOT NULL 
   ,rating int NOT NULL 
   ,reg_id varchar(10)  NOT NULL 
   ,reg_date timestamp NOT NULL 
   ,update_id varchar(10)  NOT NULL 
   ,update_date timestamp NOT NULL 
   ,delete_flg boolean DEFAULT False  NOT NULL 
   ,PRIMARY KEY(daily_report_id_pk)
);
ALTER TABLE T_FINAL_EVALUATION ADD FOREIGN KEY (daily_report_id_pk) REFERENCES T_DAILY_REPORT(id_pk);
ALTER TABLE T_FINAL_EVALUATION ADD FOREIGN KEY (evaluator_id_pk) REFERENCES M_USER_INFORMATION(id_pk);

-- Eval_Attached_File
DROP TABLE T_EVAL_ATTACHED_FILE CASCADE;

CREATE TABLE T_EVAL_ATTACHED_FILE (
    daily_report_id_pk int NOT NULL 
   ,file_path text NOT NULL 
   ,increment_num int NOT NULL
   ,uploader_id_pk int NOT NULL 
   ,reg_id varchar(10)  NOT NULL 
   ,reg_date timestamp NOT NULL 
   ,update_id varchar(10)  NOT NULL 
   ,update_date timestamp NOT NULL 
   ,delete_flg boolean DEFAULT False  NOT NULL 
   ,PRIMARY KEY(daily_report_id_pk,uploader_id_pk,increment_num)
);
ALTER TABLE T_EVAL_ATTACHED_FILE ADD FOREIGN KEY (daily_report_id_pk) REFERENCES T_DAILY_REPORT(id_pk);
ALTER TABLE T_EVAL_ATTACHED_FILE ADD FOREIGN KEY (uploader_id_pk) REFERENCES M_USER_INFORMATION(id_pk);

