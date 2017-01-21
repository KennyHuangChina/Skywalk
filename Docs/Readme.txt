************************************************************************************
*
*	�� Git log Ư��
*
1. git config --global alias.lg "log --color --graph --pretty=format:'%Cred%h%Creset -%C(yellow)%d%Creset %s %Cgreen (%cr) %C(bold blue)<%an>%Creset' --abbrev-commit --"

2. git lg

************************************************************************************
*
*	Setup ���л���
*
1. �޸� hostname Ϊ ub1604-skywalk-dev��ע�⣬Windows �з��� "\\ub1604-skywalk-"������ ��\\ub1604-skywalk-dev�� ��ʧ��

2. �� db.sql �ļ����Ƶ� \\ub1604-skywalk-\share

3. cd ~/share

4. mysql -u root -proot

5. DROP DATABASE rtdb;

6. CREATE DATABASE IF NOT EXISTS rtdb DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

7. exit

8. mysql -u root -proot rtdb < db.sql (���������ݵ������ݿ�)