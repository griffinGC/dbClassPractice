package makeTable;
import java.sql.Connection;
//import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class makeTable {

	static final String DBUrl = "jdbc:mysql://localhost:3306/?useSSL=false";
	static final String DBUser = "root";
	static final String DBPwd = "choi940126!";

	// 날짜 변경
	public static java.sql.Date changeDate(String changeFor) throws Exception {
		String date = changeFor;
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date myDate;
		myDate = formatter.parse(date);
		java.sql.Date sqlDate = new java.sql.Date(myDate.getTime());
		return sqlDate;
	}

	
	// 값 삽입쿼리
	//날짜는 Date으로 입력 
	// Player
	public static String insertPlay(String fname, Date bdate, 
			double height, double weight, String position,int time, String teamName) {
		StringBuilder sb = new StringBuilder();
		String sql = sb.append("INSERT INTO " + "Player" + " VALUES(").append("'" + fname + "',")
				.append("'" + bdate + "',")
				.append("'" + height + "',").append("'" + weight + "',").append("'" + position + "',")
				.append("'" + time + "',").append("'" + teamName+ "'").append(");").toString();
		return sql;
	};


	
	//Team
	public static String insertTeam(String dname, Date foundation, String location, String stadium, String league) {
		StringBuilder sb = new StringBuilder();
		String sql = sb.append("INSERT INTO " + "Team" + " VALUES(").append("'" + dname + "',")
				.append("'" + foundation + "',").append("'" + location + "',").append("'" + stadium + "',")
				.append("'" + league + "'").append(");")
				.toString();
		return sql;
	};

	//League
	public static String insertLeague(String dname, String level, Date fdate, Date schedule, String champion) {
		StringBuilder sb = new StringBuilder();
		String sql = sb.append("INSERT INTO " + "League" + " VALUES(").append("'" + dname + "',")
				.append("'" + level + "',").append("'" + fdate + "',").append("'" + schedule + "',")
				.append("'" + champion + "'").append(");")
				.toString();
		return sql;
	};
	

	//Coach
	public static String insertCoach(String dname,int age,String nationality, Date bdate, String team) {
		StringBuilder sb = new StringBuilder();
		String sql = sb.append("INSERT INTO " + "Coach" + " VALUES(").append("'" + dname + "',")
				.append("'" + age + "',").append("'" + nationality + "',")
				.append("'" + bdate + "',").append("'" + team + "'").append(");")
				.toString();
		return sql;
	};
	
	// Score
	public static String insertScore(String winner,String loser,  String result, String league) {
		StringBuilder sb = new StringBuilder();
		String sql = sb.append("INSERT INTO " + "Score" + " VALUES(").append("0,")
				.append("'" + winner + "',").append("'" + loser + "',").append("'" + result + "',")
				.append("'" + league + "'").append(");")
				.toString();
		return sql;
	};



	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static void main(String[] args) throws SQLException {
		Connection con = null;
		Statement stm = null;
		ResultSet rs = null;
	
	
		try {
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(DBUrl, DBUser, DBPwd);
			
			
			//커넥션 연결하고 디비생성 
			String isSQL = "DROP DATABASE IF EXISTS `FMPLAYER`;";
			String createSQL = "CREATE DATABASE FMPLAYER";
			stm = con.createStatement();
			
			//isSQL 실행 => COMPANYX라는 데이베이스 존재하면 삭제함. 
			stm.executeUpdate(isSQL);
			//createSQL에 있는 쿼리문 실행 => 데이터베이스 생성; 
			stm.executeUpdate(createSQL);
			
			//COMPANYX 선택 
			String useChoice = "USE FMPLAYER";
			stm.executeUpdate(useChoice);
			

			if(con != null) {
				System.out.println("데이터베이스 연결 성공!");
			}
			else {
				System.out.println("연결 실패");
			}
		
			//테이블 생성 
			String tablePlayer = "CREATE TABLE Player (" +
					"Name VARCHAR(50) NOT NULL," +
					"Bdate DATE,"+
					"Height DOUBLE," +
					"Weight DOUBLE," +
					"Position VARCHAR(30)," +
					"Rtime INT," +
					"Team VARCHAR(30)," +
					"PRIMARY KEY(Name, Bdate))";
		
			
			
			String tableTeam = "CREATE TABLE Team ("+
								     "Name VARCHAR(50) NOT NULL,"+
								     "Foundation DATE," +
								     "Location VARCHAR(50) NOT NULL,"+
								     "Stadium VARCHAR(50),"+
								     "League VARCHAR(50),"+
								     "PRIMARY KEY(Name))";

			String tableLeague = "CREATE TABLE League ("+
					"Name VARCHAR(50) NOT NULL," +
					"Level VARCHAR(50) NOT NULL," +
					"Fdate DATE,"+
					"Schedule DATE,"+
					"Champion VARCHAR(50)," +
					"PRIMARY KEY(Name))";
			
			
			String tableCoach = "CREATE TABLE Coach ("+
								"Name VARCHAR(50) NOT NULL," +
								"Age INT," +
								"Nationality VARCHAR(50)," +
								"Mdate DATE,"+
								"Team VARCHAR(30)," +
								"PRIMARY KEY(Name))";

			String tableScore = "CREATE TABLE Score ("+
					"ID INT NOT NULL AUTO_INCREMENT, "+
					"Winner VARCHAR(50) NOT NULL," +
					"Loser VARCHAR(50) NOT NULL," +
					"Result VARCHAR(50) NOT NULL," +
					"League VARCHAR(50)," +
					"PRIMARY KEY(ID))";			



			//테이블 생성 쿼리 
			stm.executeUpdate(tablePlayer);
			stm.executeUpdate(tableTeam);
			stm.executeUpdate(tableScore);
			stm.executeUpdate(tableCoach);
			stm.executeUpdate(tableLeague);
//			System.out.println("make table");

			//foreign key 연결
			String fkPlay1 ="ALTER TABLE Player ADD FOREIGN KEY (Team) REFERENCES Team(Name)";
			stm.executeUpdate(fkPlay1);

			
			String fkTeam1 ="ALTER TABLE Team ADD FOREIGN KEY (League) REFERENCES League(Name)";
			stm.executeUpdate(fkTeam1);
			
		
			String fkHead1 ="ALTER TABLE Coach ADD FOREIGN KEY (Team) REFERENCES Team(Name)";
			stm.executeUpdate(fkHead1);
			
			
			String fkLea1 ="ALTER TABLE League ADD FOREIGN KEY (Champion) REFERENCES Team(Name)";
			stm.executeUpdate(fkLea1);
			
			
 
			String fkScore1 ="ALTER TABLE Score ADD FOREIGN KEY (Winner) REFERENCES Team(Name)";
			stm.executeUpdate(fkScore1);
			
			String fkScore2 ="ALTER TABLE Score ADD FOREIGN KEY (Loser) REFERENCES Team(Name)";
			stm.executeUpdate(fkScore2);
			
			String fkScore3 ="ALTER TABLE Score ADD FOREIGN KEY (League) REFERENCES League(Name)";
			stm.executeUpdate(fkScore3);
	
			stm.executeUpdate("SET FOREIGN_KEY_CHECKS=0");
			
			//날짜 변경 함
			java.sql.Date sqlDate;
			java.sql.Date sqlDate2;
			sqlDate = changeDate("1993-12-15");

			//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			String insertQuery;

			//쿼리 예시
			
			//Player삽입 
//			sqlDate = changeDate("1985-02-05"); //바꿀날짜 삽
//			insertQuery = insertPlay ("Jisung park" , sqlDate, 178.0, 72.0, "MF", 10, "MU");
//			stm.executeUpdate(insertQuery);
//			System.out.print("insert player");
	         sqlDate = changeDate("1984-06-21"); //강원 FC
	         insertQuery = insertPlay ("김호준" , sqlDate, 190, 89, "GK", 436, "강원FC");
	         stm.executeUpdate(insertQuery);
	            sqlDate = changeDate("1984-07-29"); 
	         insertQuery = insertPlay ("오범석" , sqlDate, 181, 69, "DF", 2817, "강원FC");
	            stm.executeUpdate(insertQuery);
	            sqlDate = changeDate("1990-05-05"); 
	         insertQuery = insertPlay ("한용수" , sqlDate, 184,  70, "DF", 1080, "강원FC");
	            stm.executeUpdate(insertQuery);
	            sqlDate = changeDate("1989-06-20"); 
	         insertQuery = insertPlay ("김오규" , sqlDate, 183, 70, "DF", 2790, "강원FC");
	            stm.executeUpdate(insertQuery);
	            sqlDate = changeDate("1991-05-17"); 
	         insertQuery = insertPlay ("정석화" , sqlDate, 171, 63, "MF", 2688, "강원FC");
	            stm.executeUpdate(insertQuery);
	            sqlDate = changeDate("1992-01-11"); 
	         insertQuery = insertPlay ("이민수" , sqlDate, 177, 73, "MF", 58, "강원FC");
	            stm.executeUpdate(insertQuery);
	            sqlDate = changeDate("1984-05-05");
	         insertQuery = insertPlay ("황진성" , sqlDate, 177, 70, "MF", 964, "강원FC");
	            stm.executeUpdate(insertQuery);
	            sqlDate = changeDate("1984-04-23"); 
	         insertQuery = insertPlay ("정조국" , sqlDate, 186, 78, "AT", 1112, "강원FC");
	            stm.executeUpdate(insertQuery);
	            sqlDate = changeDate("1989-09-22"); 
	         insertQuery = insertPlay ("최진호" , sqlDate, 170, 70, "AT", 3, "강원FC");
	            stm.executeUpdate(insertQuery);
	            sqlDate = changeDate("1997-01-06");
	         insertQuery = insertPlay ("강지훈" , sqlDate, 177, 63, "AT", 894, "강원FC");
	            stm.executeUpdate(insertQuery);
	            sqlDate = changeDate("1991-03-25");
	         insertQuery = insertPlay ("정승용" , sqlDate, 182, 73, "AT", 2969, "강원FC");
	         stm.executeUpdate(insertQuery);
//	            System.out.print("insert player");

	         sqlDate = changeDate("1992-02-08"); //광주 FC
	         insertQuery = insertPlay ("윤평국" , sqlDate, 189, 85, "GK", 2160, "광주 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1998-01-16"); 
	         insertQuery = insertPlay ("박요한" , sqlDate, 177, 73, "DF", 2286, "광주 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1992-02-08"); 
	         insertQuery = insertPlay ("김태윤" , sqlDate, 181, 76, "DF", 1305, "광주 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1989-04-30"); 
	         insertQuery = insertPlay ("정준연" , sqlDate, 177, 68, "DF", 1681, "광주 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1989-12-04"); 
	         insertQuery = insertPlay ("완영규" , sqlDate, 185, 75, "MF", 3111, "광주 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1997-03-05"); 
	         insertQuery = insertPlay ("임민혁" , sqlDate, 167, 62, "MF", 1766, "광주 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1992-06-24"); 
	         insertQuery = insertPlay ("정영종" , sqlDate, 180, 70, "MF", 1633, "광주 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1994-03-16"); 
	         insertQuery = insertPlay ("이한두" , sqlDate, 185, 90, "MF", 1836, "광주 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1995-12-21"); 
	         insertQuery = insertPlay ("두현석" , sqlDate, 169, 65, "AT", 1404, "광주 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1996-08-12"); 
	         insertQuery = insertPlay ("나상호" , sqlDate, 172, 70, "AT", 2731,"광주 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1992-02-08"); 
	         insertQuery = insertPlay ("김정환" , sqlDate, 175, 74, "AT", 1410, "광주 FC");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("1990-04-17"); //천안 FC
	         insertQuery = insertPlay ("정대환" , sqlDate, 187, 78, "GK", 1440, "제주 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1995-03-02"); //천안 FC
	         insertQuery = insertPlay ("박종민" , sqlDate, 183, 78, "DF", 1486, "제주 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1993-06-24"); //천안 FC
	         insertQuery = insertPlay ("이수정" , sqlDate, 174, 69, "DF", 1360, "제주 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1988-02-01"); //천안 FC
	         insertQuery = insertPlay ("안재훈" , sqlDate, 187, 81, "DF", 1413, "제주 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1991-12-10"); //천안 FC
	         insertQuery = insertPlay ("조이록" , sqlDate, 180, 72, "MF", 2016, "제주 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1991-03-12"); //천안 FC
	         insertQuery = insertPlay ("안준연" , sqlDate, 180, 78, "MF", 776, "제주 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1990-11-22"); //천안 FC
	         insertQuery = insertPlay ("정현식" , sqlDate, 176, 73, "MF", 2276, "제주 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1987-04-12"); //천안 FC
	         insertQuery = insertPlay ("김대열" , sqlDate, 175, 68, "MF", 1571, "제주 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1985-09-13"); //천안 FC
	         insertQuery = insertPlay ("조형익" , sqlDate, 173, 71, "AT", 1599, "제주 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1995-05-02"); //천안 FC
	         insertQuery = insertPlay ("남희철" , sqlDate, 183, 76, "AT", 1400, "제주 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1994-03-19"); //천안 FC
	         insertQuery = insertPlay ("이강욱" , sqlDate, 180, 75, "AT", 1087, "제주 FC");
	         stm.executeUpdate(insertQuery);

	   

	            sqlDate = changeDate("1993-08-30"); //제주 유나이티트 FC
	         insertQuery = insertPlay ("이창근" , sqlDate, 186, 78, "GK", 3150, "제주 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1988-05-19"); 
	         insertQuery = insertPlay ("진권학" , sqlDate, 187, 79, "DF", 2790, "제주 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1987-08-13"); 
	         insertQuery = insertPlay ("박진포" , sqlDate, 173, 72, "DF", 2340, "제주 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1986-06-16"); 
	         insertQuery = insertPlay ("권순형" , sqlDate, 177, 73, "MF", 2294, "제주 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1993-12-17"); 
	         insertQuery = insertPlay ("류창근" , sqlDate, 172, 67, "MF", 1240, "제주 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1993-12-17");
	         insertQuery = insertPlay ("류승우" , sqlDate, 172, 67, "MF", 1240, "제주 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1993-12-16"); 
	         insertQuery = insertPlay ("진성욱" , sqlDate, 182, 82, "AT", 1109, "제주 FC");

	         sqlDate = changeDate("1991-09-25"); //대구 FC
	         insertQuery = insertPlay ("조현우" , sqlDate, 189, 73, "GK", 2514, "대구 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1996-08-04"); 
	         insertQuery = insertPlay ("김우석" , sqlDate, 187, 74, "DF", 1413, "대구 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1994-11-29"); 
	         insertQuery = insertPlay ("김종운" , sqlDate, 187, 76, "DF", 3140, "대구 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1990-08-10"); 
	         insertQuery = insertPlay ("한희운" , sqlDate, 183, 78, "DF", 2445, "대구 FC");
	         stm.executeUpdate(insertQuery);;


	         sqlDate = changeDate("1991-08-30"); //서울FC
	         insertQuery = insertPlay ("양한빈" , sqlDate, 194, 85, "GK", 3330, "서울 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1995-07-22"); 
	         insertQuery = insertPlay ("황현수" , sqlDate, 183, 80, "DF", 1170, "서울 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1988-03-10"); 
	         insertQuery = insertPlay ("고요한" , sqlDate, 170, 65, "MF", 2659, "서울 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1988-09-07"); 
	         insertQuery = insertPlay ("신지호" , sqlDate, 187, 74, "MF", 2710, "서울 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1985-07-10"); 
	         insertQuery = insertPlay ("박주영" , sqlDate, 182, 75, "AT", 958, "서울 FC");
	         stm.executeUpdate(insertQuery);
	         
	         sqlDate = changeDate("1985-07-10"); //경남 FC
	         insertQuery = insertPlay ("송정헌" , sqlDate, 191, 87, "GK", 2250, "경남 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1983-05-02"); 
	         insertQuery = insertPlay ("최재수" , sqlDate, 174, 68, "DF", 1667, "경남 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1987-06-13"); 
	         insertQuery = insertPlay ("하석민" , sqlDate, 184, 78, "MF", 1407, "경남 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1984-04-17");
	         insertQuery = insertPlay ("안성남" , sqlDate, 172, 69, "MF", 303, "경남 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1983-05-26");
	         insertQuery = insertPlay ("배기종" , sqlDate, 180, 75, "AT", 699, "경남 FC");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("1986-08-07"); //성남 천마
	         insertQuery = insertPlay ("김근배" , sqlDate, 187, 80, "GK", 1890, "경남 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1991-03-11");
	         insertQuery = insertPlay ("이학민" , sqlDate, 175, 68, "DF", 2799, "경남 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1990-12-09");
	         insertQuery = insertPlay ("저현우" , sqlDate, 174, 71, "MF", 1902, "경남 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1989-05-02");
	         insertQuery = insertPlay ("정성민" , sqlDate, 184, 77, "MF", 1488, "경남 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1994-09-13");
	         insertQuery = insertPlay ("이현리" , sqlDate, 182, 72, "AT", 462, "경남 FC");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("1986-08-07"); //안양 FC
	         insertQuery = insertPlay ("전태현" , sqlDate, 189, 85, "GK", 2880, "안양 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1993-05-06");
	         insertQuery = insertPlay ("김영찬" , sqlDate, 175, 68, "DF", 2465, "안양 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1992-12-09");
	         insertQuery = insertPlay ("최호정" , sqlDate, 182, 82, "MF", 1685, "안양 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1985-06-02");
	         insertQuery = insertPlay ("최재훈" , sqlDate, 178, 77, "MF", 2120, "안양 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1985-09-12");
	         insertQuery = insertPlay ("김희원" , sqlDate, 180, 81, "AT", 1230, "안양 FC");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("1986-04-12"); //수원 FC
	         insertQuery = insertPlay ("김다솔" , sqlDate, 187, 87, "GK", 2130, "수원 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1992-03-28");
	         insertQuery = insertPlay ("김대호" , sqlDate, 184, 88, "DF", 1297, "수원 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1987-12-01");
	         insertQuery = insertPlay ("김범영" , sqlDate, 180, 80, "MF", 3120, "수원 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1992-05-14");
	         insertQuery = insertPlay ("백성동" , sqlDate, 178, 72, "MF", 1658, "수원 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1988-07-26");
	         insertQuery = insertPlay ("이승현" , sqlDate, 180, 81, "AT", 2420, "수원 FC");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("1988-06-13"); //대전 시티즌FC
	         insertQuery = insertPlay ("박준형" , sqlDate, 187, 88, "GK", 1233, "대전 시티즌FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1991-02-07");
	         insertQuery = insertPlay ("윤준성" , sqlDate, 177, 75, "DF", 3120, "대전 시티즌FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1993-05-19");
	         insertQuery = insertPlay ("황병인" , sqlDate, 178, 76, "MF", 1988, "대전 시티즌FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1987-04-19");
	         insertQuery = insertPlay ("박수창" , sqlDate, 181, 88, "MF", 1963, "대전 시티즌FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1996-03-08");
	         insertQuery = insertPlay ("김찬희" , sqlDate, 184, 84, "AT", 654, "대전 시티즌FC");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("1989-05-14"); //천안 시티FC
	         insertQuery = insertPlay ("정대환" , sqlDate, 188, 85, "GK", 2254, "천안 시티FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1990-10-12");
	         insertQuery = insertPlay ("안재훈" , sqlDate, 178, 78, "DF", 1232, "천안 시티FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1988-07-08");
	         insertQuery = insertPlay ("민훈지" , sqlDate, 180, 88, "MF", 1265, "천안 시티FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1988-02-14");
	         insertQuery = insertPlay ("이현창" , sqlDate, 177, 67, "MF", 1850, "천안 시티FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1986-09-06");
	         insertQuery = insertPlay ("조형익" , sqlDate, 188, 85, "AT", 1200, "천안 시티FC");
	         stm.executeUpdate(insertQuery);
	         
	         sqlDate = changeDate("1987-06-14"); //부천 FC 1995
	         insertQuery = insertPlay ("이기현" , sqlDate, 187, 88, "GK", 2654, "부천 FC 1995");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1993-11-17");
	         insertQuery = insertPlay ("장순혁" , sqlDate, 178, 77, "DF", 1754, "부천 FC 1995");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1988-07-08");
	         insertQuery = insertPlay ("박군" , sqlDate, 179, 80, "MF", 1265, "부천 FC 1995");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1987-02-03");
	         insertQuery = insertPlay ("문기한" , sqlDate, 177, 67, "MF", 3205, "부천 FC 1995");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1994-06-08");
	         insertQuery = insertPlay ("정민현" , sqlDate, 185, 87, "AT", 2200, "부천 FC 1995");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("1988-07-20"); //울산 현대
	         insertQuery = insertPlay ("김용대" , sqlDate, 187, 85, "GK", 3333, "울산 현대");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1994-11-22");
	         insertQuery = insertPlay ("강민수" , sqlDate, 180, 80, "DF", 2200, "울산 현대");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1987-08-23");
	         insertQuery = insertPlay ("김인성" , sqlDate, 184, 82, "MF", 2341, "울산 현대");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1993-03-09");
	         insertQuery = insertPlay ("황일수" , sqlDate, 181, 83, "MF", 1520, "울산 현대");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1988-08-06");
	         insertQuery = insertPlay ("이근호" , sqlDate, 178, 76, "AT", 1264, "울산 현대");
	         stm.executeUpdate(insertQuery);
	         
	         sqlDate = changeDate("1990-06-26"); //포항 스틸러스
	         insertQuery = insertPlay ("류원우" , sqlDate, 190, 88, "GK", 2564, "포항 스틸러스");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1988-11-15");
	         insertQuery = insertPlay ("김광석" , sqlDate, 175, 77, "DF", 1524, "포항 스틸러스");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1992-06-08");
	         insertQuery = insertPlay ("배슬기" , sqlDate, 178, 75, "MF", 785, "포항 스틸러스");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1994-01-26");
	         insertQuery = insertPlay ("이석현" , sqlDate, 180, 80, "MF", 1685, "포항 스틸러스");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1990-08-15");
	         insertQuery = insertPlay ("이근호" , sqlDate, 188, 85, "AT", 3321, "포항 스틸러스");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("1992-05-21"); //아산 무궁화
	         insertQuery = insertPlay ("양형모" , sqlDate, 189, 88, "GK", 1249, "아산 무궁화");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1987-12-15");
	         insertQuery = insertPlay ("김용래" , sqlDate, 175, 78, "DF", 1254, "아산 무궁화");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1985-07-06");
	         insertQuery = insertPlay ("김준수" , sqlDate, 177, 80, "MF", 2456, "아산 무궁화");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1988-09-26");
	         insertQuery = insertPlay ("이한샘" , sqlDate, 187, 85, "MF", 654, "아산 무궁화");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1988-05-09");
	         insertQuery = insertPlay ("김종국" , sqlDate, 184, 88, "AT", 1542, "아산 무궁화");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("1991-11-21"); //인천 FC
	         insertQuery = insertPlay ("정산" , sqlDate, 185, 88, "GK", 2154, "인천 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1987-12-15");
	         insertQuery = insertPlay ("김용환" , sqlDate, 175, 78, "DF", 654, "인천 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1985-08-07");
	         insertQuery = insertPlay ("이운표" , sqlDate, 180, 80, "MF", 1456, "인천 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1988-09-30");
	         insertQuery = insertPlay ("최종환" , sqlDate, 177, 75, "MF", 1658, "인천 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("1996-11-13");
	         insertQuery = insertPlay ("고슬기" , sqlDate, 178, 76, "AT", 952, "인천 FC");
	         stm.executeUpdate(insertQuery);
				System.out.print("insert team");		
	       
	            
//				K리그 1
//				K리그 2
//				내셔널 리그
//				K3 리그 어드밴스
				
			//Team삽입

			sqlDate = changeDate("2008-12-18"); //바꿀날짜 삽
			insertQuery = insertTeam ("강원 FC" , sqlDate, "강릉", "춘천 종합 운동장", "K리그 1");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2010-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("광주 FC" , sqlDate, "광주", "광주 월드컵 경기장", "K리그 2");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2008-01-09"); //바꿀날짜 삽
			insertQuery = insertTeam ("천안 FC" , sqlDate, "천안", "천안 축구 센터", "내셔널 리그");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("1982-12-17"); //바꿀날짜 삽
			insertQuery = insertTeam ("제주 FC" , sqlDate, "제주", "제주 월드컵 경기장", "K리그 1");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2002-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("대구 FC" , sqlDate, "대구", "대구 스타디움", "K리그 1");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("1983-12-22"); //바꿀날짜 삽
			insertQuery = insertTeam ("서울 FC" , sqlDate, "서울", "서울 월드컵 경기장", "K리그 1");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2006-01-17"); //바꿀날짜 삽
			insertQuery = insertTeam ("경남 FC" , sqlDate, "경상남도", "창원 축구 센터", "K리그 1");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("1989-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("성남 천마" , sqlDate, "성남", "탄천 종합 운동장", "K리그 1");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2013-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("안양 FC" , sqlDate, "안양", "안양 종합 운동장", "K리그 2");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2003-03-15"); //바꿀날짜 삽
			insertQuery = insertTeam ("수원 FC" , sqlDate, "수원", "수원 종합 운동장", "내셔널 리그");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("1997-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("대전 시티즌FC" , sqlDate, "대전", "대전 월드컵 경기장", "K리그 2");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2007-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("천안 시티FC" , sqlDate, "천안", "천안 축구 센터", "내셔널 리그");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2007-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("부천 FC 1955" , sqlDate, "부천", "부천 종합 운동장", "K리그 2");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("1983-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("울산 현대" , sqlDate, "울산", "울산 문수 축구 경기장", "K리그 1");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("1973-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("포항 스틸러스" , sqlDate, "포항", "포항 스틸야드", "K리그 1");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2017-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("아산 무궁화" , sqlDate, "아산", "이순신 종합 운동장", "K리그 2");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2003-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("인천 FC" , sqlDate, "인천", "인천 축구 전용 경기장", "K리그 1");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2003-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("인천 유나이티드 FC" , sqlDate, "인천", "인천 축구 전용 경기장", "K리그 1");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("1985-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("상주 상무 피닉스 FC" , sqlDate, "상주", "상주 경기장", "K리그 1");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("1995-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("수원 삼성 블루윙즈 FC" , sqlDate, "수원", "수원 월드컵 경기장", "K리그 1");
			stm.executeUpdate(insertQuery);
			
//			System.out.println("insert team20");
			
			sqlDate = changeDate("1993-12-18"); //바꿀날짜 삽
			insertQuery = insertTeam ("전북 현대 모터스 FC" , sqlDate, "전주", "전주 월드컵 경기장", "K리그 1");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("1994-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("전남 드래곤즈" , sqlDate, "광양", "광양 월드컵 경기장", "K리그 1");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2017-01-09"); //바꿀날짜 삽
			insertQuery = insertTeam ("안산 그리너스 FC" , sqlDate, "안산", "안산와 스타디움", "K리그 2");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2014-12-17"); //바꿀날짜 삽
			insertQuery = insertTeam ("서울 e-land FC" , sqlDate, "서울", "서울 올림픽 경기장", "K리그 2");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("1983-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("부산 아이파크" , sqlDate, "부산", "부산 구덕 스타디움", "K리그 2");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2006-12-22"); //바꿀날짜 삽
			insertQuery = insertTeam ("부산 교통 공사 FC" , sqlDate, "부산", "부산 구덕 스타디움", "내셔널 리그");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2005-01-17"); //바꿀날짜 삽
			insertQuery = insertTeam ("창원시 FC" , sqlDate, "창원", "창원 축구 센터", "내셔널 리그");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("1943-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("대전 코레일" , sqlDate, "대전", "대전 한밭 스타디움", "내셔널 리그");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("1999-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("강릉시 FC" , sqlDate, "강릉", "강릉 스타디움", "내셔널 리그");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2007-03-15"); //바꿀날짜 삽
			insertQuery = insertTeam ("김해시 FC" , sqlDate, "김해", "김해 시민 운동장", "내셔널 리그");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("1962-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("경주 한국수력원자력 FC" , sqlDate, "경주", "경주 시민 운동장", "내셔널 리그");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2009-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("목포시 FC" , sqlDate, "목표", "목포 국제 풋볼 센터", "내셔널 리그");
			stm.executeUpdate(insertQuery);
			
			sqlDate = changeDate("2002-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("청주 FC" , sqlDate, "청주", "용정 풋볼 센터", "K3 리그 어드밴스");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2004-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("청주시 FC" , sqlDate, "청주", "청주 스타디움", "K3 리그 어드밴스");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2000-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("평택 시티즌 FC" , sqlDate, "평택", "평택 스타디움", "K3 리그 어드밴스");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2017-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("양평 FC" , sqlDate, "양평", "용문생활체육공원", "K3 리그 어드밴스");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2010-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("춘천 FC" , sqlDate, "춘천", "춘천 송암 경기장", "K3 리그 어드밴스");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2013-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("김포시민 FC" , sqlDate, "김포", "김포 스타디움", "K3 리그 어드밴스");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2007-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("경주시민 FC" , sqlDate, "경주", "경주 시민 경기장", "K3 리그 어드밴스");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2013-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("화성 FC" , sqlDate, "화성", "화성 경기장", "K3 리그 어드밴스");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2009-12-18"); //바꿀날짜 삽
			insertQuery = insertTeam ("인천시민 FC" , sqlDate, "인천", "인천시 경기장", "K3 리그 어드밴스");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("1994-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("전주시민 FC" , sqlDate, "전주", "전주대 경기장", "K3 리그 어드밴스");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2017-01-09"); //바꿀날짜 삽
			insertQuery = insertTeam ("중랑 코러스 무스탕 FC" , sqlDate, "서울", "중랑구 스포트 센터", "K3 리그 어드밴스");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2007-12-17"); //바꿀날짜 삽
			insertQuery = insertTeam ("포천 FC" , sqlDate, "포천", "포천 경기장", "K3 리그 어드밴스");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("1996-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("아주대 FC" , sqlDate, "수원", "", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("1996-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("충북대 FC" , sqlDate, "청주", "충북대 풋살장", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("1996-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("용인대 FC" , sqlDate, "용인", "", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("1996-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("용인시 FC" , sqlDate, "용인", "용인 풋살 센터", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("1996-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("유한화학 FC" , sqlDate, "안산", "", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("1996-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("청주시민 FC" , sqlDate, "청주", "", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2010-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("충남영광 FC" , sqlDate, "영광", "영광 경기장", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("1996-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("대구대 FC" , sqlDate, "대구", "", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("1996-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("배재대 FC" , sqlDate, "대전", "", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("1996-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("부천 SK" , sqlDate, "부천", "", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("1996-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("댄소 코리아" , sqlDate, "", "", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("1996-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("동아대 FC" , sqlDate, "", "", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("1996-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("대우조선해양 FC" , sqlDate, "", "", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("1980-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("고양 자이크로 FC" , sqlDate, "고양", "고양 경기장", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("1996-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("광주대 FC" , sqlDate, "광주", "", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("1996-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("경기 중앙대 FC" , sqlDate, "", "", "Other");
			stm.executeUpdate(insertQuery);
//			System.out.println("insert team60");
			
			sqlDate = changeDate("2006-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("강원 한라대 FC" , sqlDate, "강릉", "", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2006-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("부산 부경대 FC" , sqlDate, "부산", "", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2006-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("한양대 FC" , sqlDate, "서월", "구미 시민 운동장", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2006-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("홍천 IDU FC" , sqlDate, "홍천", "", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2006-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("SK Hynix" , sqlDate, "", "", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2006-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("인천대 FC" , sqlDate, "인천", "인천대 경기장", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2006-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("제주시 HALL FC" , sqlDate, "제주", "", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2006-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("전북 서남대 FC" , sqlDate, "전북", "", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2006-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("전주 온고을 FC" , sqlDate, "전주", "", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2006-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("중원대 FC" , sqlDate, "괴산", "", "Other");
			stm.executeUpdate(insertQuery);
			
			sqlDate = changeDate("2006-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("가톨릭 관동대 FC" , sqlDate, "강릉", "", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2006-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("경희대 FC" , sqlDate, "", "용인 축구 센터", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2006-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("LG전자" , sqlDate, "", "", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2007-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("남양주 유나이티드 FC" , sqlDate, "남양주", "남양주 경기장", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2012-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("파주시민 FC" , sqlDate, "파주", "파주 공용 경기장", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2006-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("평창 FC" , sqlDate, "평창", "평창 경기장", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2006-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("삼척 신우 전자 FC" , sqlDate, "삼척", "", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2006-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("상지대 FC" , sqlDate, "", "", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2006-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("세아 베스틸 FC" , sqlDate, "군산", "", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2006-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("세종 산업 울산 FC" , sqlDate, "울산 ", "", "Other");
			stm.executeUpdate(insertQuery);
//			System.out.print("insert team 80!");
			
			sqlDate = changeDate("2006-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("조선대 FC" , sqlDate, "", "", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2006-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("숭실대 FC" , sqlDate, "서울", "숭실대 경기장", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2006-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("선문대 FC" , sqlDate, "", "선문대 경기장", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2013-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("FC 의정부" , sqlDate, "의정부", "의정부 경기장", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2006-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("울산대 FC" , sqlDate, "울산", "", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2007-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("양주 시민 FC" , sqlDate, "양주", "양주 고덕 경기장", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2006-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("연세대 FC" , sqlDate, "서울", "연세대 경기장", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2006-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("사이버한국외대 FC" , sqlDate, "서울", "", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2006-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("단국대 FC" , sqlDate, "", "", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2006-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("동의대 FC" , sqlDate, "", "", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2006-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("동국대 FC" , sqlDate, "서울", "", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2006-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("강원 송호대 FC" , sqlDate, "", "", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2006-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("경기대 FC" , sqlDate, "수원", "", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2006-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("한남대 FC" , sqlDate, "", "", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2006-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("호남대 FC" , sqlDate, "광주", "", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2006-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("홍익대 FC" , sqlDate, "서울", "", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2006-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("건국대 FC" , sqlDate, "서울", "김천 경기장", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2006-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("고려대 FC" , sqlDate, "서울", "고려대 안암 인조 경기장", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2006-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("광운대 FC" , sqlDate, "서울", "", "Other");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2006-11-01"); //바꿀날짜 삽
			insertQuery = insertTeam ("성균관대 FC" , sqlDate, "", "", "Other");
			stm.executeUpdate(insertQuery);
			System.out.println("insert team 100!");
			
			
			//Leauge삽입 
			sqlDate = changeDate("1983-01-01"); //바꿀날짜 창립일 
			sqlDate2 = changeDate("2018-03-01"); //바꿀날짜 스케줄 
			insertQuery = insertLeague ("K리그 1" , "1", sqlDate, sqlDate2, "전북 현대");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2013-01-01"); //바꿀날짜 창립일 
			sqlDate2 = changeDate("2018-03-03"); //바꿀날짜 스케줄 
			insertQuery = insertLeague ("K리그 2" , "2", sqlDate, sqlDate2, "아산 무궁화");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2003-01-01"); //바꿀날짜 창립일
			sqlDate2 = changeDate("2018-03-17"); //바꿀날짜 스케줄 
			insertQuery = insertLeague ("내셔널 리그" , "3", sqlDate, sqlDate2, "경주 한국수력원자력 축구단");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2007-01-01"); //바꿀날짜 창립일
			sqlDate2 = changeDate("2018-03-24"); //바꿀날짜 스케줄 
			insertQuery = insertLeague ("K3 리그 어드밴스" , "4", sqlDate, sqlDate2, "경주 시민축구단");
			stm.executeUpdate(insertQuery);
			sqlDate = changeDate("2099-01-01"); //바꿀날짜 창립일
			sqlDate2 = changeDate("2099-03-24"); //바꿀날짜 스케줄 
			insertQuery = insertLeague ("Other" , "99", sqlDate, sqlDate2, "");
			stm.executeUpdate(insertQuery);
			
			
			System.out.println("insert league");
	
	         //Coach삽입 
//	         sqlDate = changeDate("1965-07-27"); //바꿀날짜 삽
//	         insertQuery = insertCoach ("이름" , 나이, 국적, "date", "팀");
//	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-08-01"); 
	         insertQuery = insertCoach ("김병수" ,  48, "대한민국", sqlDate, "강원 FC");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2017-08-01"); 
	         insertQuery = insertCoach ("최송환" ,  48, "대한민국", sqlDate, "제주 FC");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-09-01"); 
	         insertQuery = insertCoach ("김종부" ,  53, "대한민국", sqlDate, "경남 FC");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-07-01"); 
	         insertQuery = insertCoach ("정갑석" ,  50, "대한민국", sqlDate, "부천 FC 1995");
	         stm.executeUpdate(insertQuery);
	         
	         sqlDate = changeDate("2018-09-01"); 
	         insertQuery = insertCoach ("최강희" ,  47, "대한민국", sqlDate, "전북 현대 모터스");
	         stm.executeUpdate(insertQuery);
	         
	         sqlDate = changeDate("2018-11-01"); 
	         insertQuery = insertCoach ("안드레" ,  40, "브라질", sqlDate, "대구 FC");
	         stm.executeUpdate(insertQuery);
	         
	         sqlDate = changeDate("2018-12-01"); 
	         insertQuery = insertCoach ("김태완" ,  45, "대한민국", sqlDate, "상주 상무");
	         stm.executeUpdate(insertQuery);
	         
	         sqlDate = changeDate("2018-11-01"); 
	         insertQuery = insertCoach ("서정원" ,  47, "대한민국", sqlDate, "수원 블루윙즈");
	         stm.executeUpdate(insertQuery);
	         
	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("김도훈" ,  52, "대한민국", sqlDate, "울산 FC");
	         stm.executeUpdate(insertQuery);
	         
	         sqlDate = changeDate("2018-07-01"); 
	         insertQuery = insertCoach ("예른 안데르센" ,  43, "독일", sqlDate, "인천 FC");
	         stm.executeUpdate(insertQuery);
	         
	         sqlDate = changeDate("2018-07-01"); 
	         insertQuery = insertCoach ("김인완" ,  49, "대한민국", sqlDate, "전남 드레곤즈");
	         stm.executeUpdate(insertQuery);
	         
	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("조성환" ,  48, "대한민국", sqlDate, "제주 FC");
	         stm.executeUpdate(insertQuery);
	         
	         sqlDate = changeDate("2018-08-01"); 
	         insertQuery = insertCoach ("최순호" ,  52, "대한민국", sqlDate, "포항 스틸러스");
	         stm.executeUpdate(insertQuery);
	         
	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("박진섭" ,  48, "대한민국", sqlDate, "광주 FC");
	         stm.executeUpdate(insertQuery);
	         
	         sqlDate = changeDate("2018-09-01"); 
	         insertQuery = insertCoach ("고종수" ,  56, "대한민국", sqlDate, "대전 시티즌");
	         stm.executeUpdate(insertQuery);
	         
	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("조민혁" ,  42, "대한민국", sqlDate, "부전 FC 1995");
	         stm.executeUpdate(insertQuery);
	         
	         sqlDate = changeDate("2018-09-01"); 
	         insertQuery = insertCoach ("인창수" ,  41, "대한민국", sqlDate, "서울 이랜드 FC");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("남기일" ,  49, "대한민국", sqlDate, "성남 FC");
	         stm.executeUpdate(insertQuery);
	         
	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("김대의" ,  53, "대한민국", sqlDate, "수원 FC");
	         stm.executeUpdate(insertQuery);
	         sqlDate = changeDate("2018-08-01"); 
	         insertQuery = insertCoach ("최윤겸" ,  50, "대한민국", sqlDate, "부산 아이파크");
	         stm.executeUpdate(insertQuery);
	         
	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("박동혁" ,  49, "대한민국", sqlDate, "아산 무궁화");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-07-01"); 
	         insertQuery = insertCoach ("임완섭" ,  40, "대한민국", sqlDate, "안산 그리너스 FC");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("고정운" ,  50, "대한민국", sqlDate, "안양 FC");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-08-01"); 
	         insertQuery = insertCoach ("Julen Lopetegui" ,  52, "독일 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-11-01"); 
	         insertQuery = insertCoach ("최민영" ,  42, "대한민국 ", sqlDate, "AJou Univ");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01");  //-> 20
	         insertQuery = insertCoach ("배가정" ,  30, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-12-01"); 
	         insertQuery = insertCoach ("홍석중" ,  40, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);
	         
	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("정한서" ,  50, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("나세현" ,  41, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("유정연" ,  46, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-01-01"); 
	         insertQuery = insertCoach ("정영성" ,  47, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-02-01"); 
	         insertQuery = insertCoach ("박민국" ,  48, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("장영실" ,  45, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-03-01"); 
	         insertQuery = insertCoach ("이순신" ,  45, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("황진영" ,  44, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("신명성" ,  57, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-01-01"); 
	         insertQuery = insertCoach ("강영환" ,  55, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);
	         
	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("최재민" ,  54, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("박효신" ,  53, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-05-01"); 
	         insertQuery = insertCoach ("정향수" ,  52, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("곽도형" ,  54, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-06-01"); 
	         insertQuery = insertCoach ("조성민" ,  55, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("최지운" ,  35, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-08-01"); 
	         insertQuery = insertCoach ("조연진" ,  34, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-09-01"); 
	         insertQuery = insertCoach ("문옥빈" ,  61, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01");  //-> 40
	         insertQuery = insertCoach ("최영식" ,  45, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("이보영" ,  55, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);
	         
	         sqlDate = changeDate("2018-08-01"); 
	         insertQuery = insertCoach ("조아성" ,  44, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01");
	         insertQuery = insertCoach ("조현민" ,  35, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("박은석" ,  39, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-09-01"); 
	         insertQuery = insertCoach ("하지명" ,  49, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("김나영" ,  47, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("박소정" ,  48, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-07-01"); 
	         insertQuery = insertCoach ("조현국" ,  46, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("하내진" ,  44, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-08-01"); 
	         insertQuery = insertCoach ("허수진" ,  55, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("노승윤" ,  52, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);
	         
	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("태성" ,  57, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-11-01"); 
	         insertQuery = insertCoach ("장수민" ,  52, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("김아랑" ,  52, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("최미산" ,  42, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-12-01"); 
	         insertQuery = insertCoach ("장수영" ,  52, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-11-01"); 
	         insertQuery = insertCoach ("홍원수" ,  45, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("나보령" ,  52, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-07-01"); 
	         insertQuery = insertCoach ("배선민" ,  52, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("미이령" ,  52, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-07-01");  
	         insertQuery = insertCoach ("최승진" ,  65, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("유혜진" ,  52, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);
	         
	         sqlDate = changeDate("2018-08-01"); 
	         insertQuery = insertCoach ("권나진" ,  52, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("김성주" ,  53, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("연수현" ,  45, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-09-01"); 
	         insertQuery = insertCoach ("황준표" ,  44, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("권수영" ,  52, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-12-01"); 
	         insertQuery = insertCoach ("이광수" ,  46, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("나보성" ,  52, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("김영선" ,  48, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-11-01"); 
	         insertQuery = insertCoach ("홍원경" ,  52, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("정주영" ,  49, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);
	         
	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("이지영" ,  52, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-12-01"); 
	         insertQuery = insertCoach ("김선주" ,  50, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("이지아" ,  52, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-05-01"); 
	         insertQuery = insertCoach ("곽도선" ,  54, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-06-01"); 
	         insertQuery = insertCoach ("조성영" ,  52, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("김경란" ,  52, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-06-01"); 
	         insertQuery = insertCoach ("이헤정" ,  54, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("조성미" ,  52, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("배선예" ,  45, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-08-01");  //-> 20
	         insertQuery = insertCoach ("노인성" ,  52, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("서유리" ,  44, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);
	         
	         sqlDate = changeDate("2018-03-01"); 
	         insertQuery = insertCoach ("노인석" ,  52, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("박채황" ,  52, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("강금한" ,  46, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-02-01"); 
	         insertQuery = insertCoach ("강한남" ,  52, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("천수연" ,  48, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("손향현" ,  52, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-05-01"); 
	         insertQuery = insertCoach ("박패선" ,  52, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-04-01"); 
	         insertQuery = insertCoach ("강현성" ,  47, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("고은정" ,  52, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-06-01"); 
	         insertQuery = insertCoach ("심정은" ,  52, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);
	         
	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("이미형" ,  48, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("심저은" ,  49, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("이미훈" ,  52, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-11-01"); 
	         insertQuery = insertCoach ("이혜정" ,  48, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("최성현" ,  52, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("전은주" ,  52, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-12-01"); 
	         insertQuery = insertCoach ("지운성" ,  52, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("김미옥" ,  46, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("김희경" ,  49, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);
	         
	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("전은수" ,  52, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-04-01"); 
	         insertQuery = insertCoach ("나현" ,  52, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("조가연" ,  45, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-05-01"); 
	         insertQuery = insertCoach ("성성영" ,  52, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("천조국" ,  52, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("천성문" ,  49, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-03-01"); 
	         insertQuery = insertCoach ("박은혜" ,  52, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("정혜수" ,  52, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("과은정" ,  47, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("정과연" ,  52, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);
	         
	         sqlDate = changeDate("2018-08-01"); 
	         insertQuery = insertCoach ("이시영" ,  52, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-09-01"); 
	         insertQuery = insertCoach ("정환수" ,  46, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("강향수" ,  52, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("강자영" ,  52, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-08-01"); 
	         insertQuery = insertCoach ("최민우" ,  65, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("정다암" ,  52, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-07-01"); 
	         insertQuery = insertCoach ("정설암" ,  52, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-09-01"); 
	         insertQuery = insertCoach ("곽이연" ,  42, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

	         sqlDate = changeDate("2018-10-01"); 
	         insertQuery = insertCoach ("망이연" ,  41, "대한민국 ", sqlDate, "무소속");
	         stm.executeUpdate(insertQuery);

			
		         System.out.println("insert coach");
			
			
			//Score삽입
			insertQuery = insertScore("전북 현대", "수원 삼성", "2:0", "K리그 1");
			stm.executeUpdate(insertQuery);
			insertQuery = insertScore("전북 현대", "울산 현대", "3:1", "K리그 1");
			stm.executeUpdate(insertQuery);
			
	         insertQuery = insertScore("인천 FC", "전남 FC", "3:1", "K리그 1");
	         stm.executeUpdate(insertQuery);
	         
	         insertQuery = insertScore("대구 FC", "강원 FC", "0:1", "K리그 1");
	         stm.executeUpdate(insertQuery);
//	         System.out.print("insert score");

	         insertQuery = insertScore("상주 FC", "서울 FC", "1:0", "K리그 1");
	         stm.executeUpdate(insertQuery);
//	         System.out.print("insert score");

	         insertQuery = insertScore("제주 FC", "서울 FC", "0:2", "K리그 1");
	         stm.executeUpdate(insertQuery);
//	         System.out.print("insert score");

	         insertQuery = insertScore("울산 FC", "포항 FC", "3:1", "K리그 1");
	         stm.executeUpdate(insertQuery);
//	         System.out.print("insert score");

	         insertQuery = insertScore("전북 FC", "경남 FC", "1:1", "K리그 1");
	         stm.executeUpdate(insertQuery);
//	         System.out.print("insert score");

	         insertQuery = insertScore("서울 FC", "부산 FC", "3:1", "K리그 1");
	         stm.executeUpdate(insertQuery);
//	         System.out.print("insert score");

	         insertQuery = insertScore("서울 FC", "부산 FC", "1:1", "K리그 1");
	         stm.executeUpdate(insertQuery);
//	         System.out.print("insert score");

	         insertQuery = insertScore("경남 FC", "제주 FC", "0:1", "K리그 1");
	         stm.executeUpdate(insertQuery);
//	         System.out.print("insert score");

	         insertQuery = insertScore("인천 FC", "상주 FC", "2:1", "K리그 1");
	         stm.executeUpdate(insertQuery);
//	         System.out.print("insert score");

	         insertQuery = insertScore("대구 FC", "서울 FC", "1:1", "K리그 1");
	         stm.executeUpdate(insertQuery);
//	         System.out.print("insert score");

	         insertQuery = insertScore("포항 FC", "수원 FC", "1:3", "K리그 1");
	         stm.executeUpdate(insertQuery);
//	         System.out.print("insert score");

	         insertQuery = insertScore("강원 FC", "전남 FC", "2:1", "K리그 1");
	         stm.executeUpdate(insertQuery);
//	         System.out.print("insert score");

	         insertQuery = insertScore("수원 FC", "울산 FC", "0:1", "K리그 1");
	         stm.executeUpdate(insertQuery);
//	         System.out.print("insert score");

	         insertQuery = insertScore("전남 FC", "강원 FC", "1:0", "K리그 1");
	         stm.executeUpdate(insertQuery);
//	         System.out.print("insert score");

	         insertQuery = insertScore("수원 FC", "울산 FC", "3:3", "K리그 1");
	         stm.executeUpdate(insertQuery);
//	         System.out.print("insert score");

	         insertQuery = insertScore("서울 FC", "전남 FC", "2:1", "K리그 1");
	         stm.executeUpdate(insertQuery);
//	         System.out.print("insert score");

	         insertQuery = insertScore("대구 FC", "상주 FC", "0:0", "K리그 1");
	         stm.executeUpdate(insertQuery);
//	         System.out.print("insert score");

	         insertQuery = insertScore("서울 FC", "인천 FC", "2:1", "K리그 1");
	         stm.executeUpdate(insertQuery);
//	         System.out.print("insert score");

	         insertQuery = insertScore("포항 FC", "전북 FC", "3:1", "K리그 1");
	         stm.executeUpdate(insertQuery);
//	         System.out.print("insert score");

	         insertQuery = insertScore("대구 FC", "수원 FC", "0:0", "K리그 1");
	         stm.executeUpdate(insertQuery);
//	         System.out.print("insert score");

	         insertQuery = insertScore("포항 FC", "전남 FC", "0:1", "K리그 1");
	         stm.executeUpdate(insertQuery);
//	         System.out.print("insert score");

	         insertQuery = insertScore("포항 FC", "전남 FC", "2:1", "K리그 1");
	         stm.executeUpdate(insertQuery);
//	         System.out.print("insert score");

	         insertQuery = insertScore("인천 FC", "서울 FC", "2:1", "K리그 1");
	         stm.executeUpdate(insertQuery);
//	         System.out.print("insert score");
			System.out.println("insert score");

			
			
			System.out.println("Success!!");
		}catch(Exception ex) {
			System.out.println("SQLExcetpion : " + ex.getMessage());
			
		}
		finally {
//			stm.close();
//			con.close();
		}
	}
}
