package FMPlayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class FMPlayer {

	public static String allSQL;
	public static String team1Point;
	public static String team2Point;
	public static double total1RunTime = 26833;
	public static double total2RunTime = 23483;
	public static double total3RunTime = 16421;
	
	public static Connection getConnection() {
		try {
			
			String DBUrl = "jdbc:mysql://localhost:3306/?useSSL=false";
			String DBUser = "root";
			String DBPwd = "choi940126!";
			Class.forName("com.mysql.cj.jdbc.Driver");
			return DriverManager.getConnection(DBUrl, DBUser, DBPwd);
			
		
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static String searchPlayer(String name) {
		String query = null;
		
		query = "SELECT * FROM Player WHERE Name='"+name+"';";
//		System.out.println(query);
		
		return query;
	}
	
	public static String getTeamPlayer(String name) {
		String add = null;
		add = "SELECT * FROM Player WHERE Team='"+name+"';";
		return add;
	}
	
	public static String addIntoTable(String table, String name, String rtime, String teamname) {
		StringBuilder sb = new StringBuilder();
//		String sql = sb.append("INSERT INTO " + table + " VALUES(").append("0,"+"'" + name + "',")
		String sql = sb.append("INSERT INTO " + table + " VALUES(").append("0,"+"'" + name + "',")
				.append("'" + teamname + "',").append("'" + rtime + "'").append(");")
				.toString();
//		System.out.println(sql);
		return sql;
	}
	
	public static String customTeam(String name) {
		String newTable = null;
		newTable = "CREATE TABLE "+name+" (" +
				"ID INT NOT NULL AUTO_INCREMENT, "+
				"Name VARCHAR(50) NOT NULL," +
				"Team_name VARCHAR(30)," +
				"RunningTime VARCHAR(30)," +
				"PRIMARY KEY(ID))";		
		return newTable;
	}
	

	
	public static String searchTeam(String name) {
		String query = null;
		
		query = "SELECT * FROM Team WHERE Name='"+name+"';";
		
		return query;
	}
	
	public static String searchTeamMember(String name) {
		String query = null;
		query = "SELECT Name FROM Player WHERE Team_name='"+name+"';";
		return query;
	}
	
	public static String getTeamRuntime(String name) {
		String point = null;
		point = "SELECT SUM(RunningTime) FROM "+name+";";
		
		return point;
	}
	
	public static String searchCoach(String name) {
		String query = null;
		
		query = "SELECT * FROM Coach WHERE Name='"+name+"';";
//		System.out.println(query);
		
		return query;
	}
	
	public static String searchWScore(String name) {
		String query = null;
		query = "SELECT * FROM Score WHERE Winner='"+name+"';";
		return query;
	}
	
	public static String searchLScore(String name) {
		String query = null;
		query = "SELECT * FROM Score WHERE Loser='"+name+"';";
		return query;
	}
	
	
	
	public static void main(String args[])throws Exception {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			Connection conn = getConnection();
			Statement stm = conn.createStatement();
			ResultSet rs = null;
			while(true) {
				
			System.out.println("값을 입력해주세요!");
			System.out.println("1. 선수검색 및 팀 커스터마이징!");
			System.out.println("2. 팀 검색 및 커스텀 팀과 비교!");
			System.out.println("3. 감독 검색!");
			System.out.println("4. 경기내용 검색!");
			System.out.println("종료하고 싶으면 exit 를 입력해 주세요!");
			String select;
			Scanner scan = new Scanner(System.in);
			select = scan.nextLine();
			String useTable = "USE FMPLAYER";
			stm.executeQuery(useTable);
			
			switch(select){
				case "1" :
					System.out.println("선수 검색을 하고 싶으면 검색 을 커스터마이징 하고 싶으면 커스텀 을 입력해주세요!");
					String selectFunc = scan.nextLine();
					switch(selectFunc) {
					case "검색" :
						System.out.println("선수 이름을 입력해주세요!");
						String name = scan.nextLine();
						String result = searchPlayer(name);
						rs = stm.executeQuery(result);
						while(rs.next())
						{
							int i = 1;
							while(i < 8) {
								System.out.print(rs.getString(i) + "\t");
								i++;
							}
							System.out.println();
						}
						System.out.println("선수를 추가하고 싶으시다면 추가하고싶은 테이블명 입력해주세요!");
						System.out.println("그렇지 않다면 Enter를 눌러주세요!");
						ResultSet newRs = stm.executeQuery(result);
						String selectAddTable = scan.nextLine();
//						System.out.println(result);
						while(newRs.next())
						{
							allSQL = addIntoTable(selectAddTable, name, newRs.getString(6), newRs.getString(7));
						};
//						System.out.println("update?");	
//						System.out.println(allSQL);
						stm.executeUpdate(allSQL);
						System.out.println("추가되었습니다!!");
						break;
						
					case "커스텀" :
						System.out.println("커스텀하고 싶은 이름을 입력하세요!");
						String teamName = scan.nextLine();
						String sql = customTeam(teamName);
						stm.executeUpdate(sql);
						System.out.println("팀이 생성되었습니다.");
						break;
					}
					break;
				case "2" :
					System.out.println("팀 검색을 원할시 검색을 입력해주시고 비교를 원할시 비교를 입력해 주세요!");
					String teamSelect = scan.nextLine();
					switch(teamSelect)
					{
					case "검색":
						System.out.println("팀 이름을 입력해주세요!");
						String teamName = scan.nextLine();
						String result = searchTeam(teamName);
						rs = stm.executeQuery(result);
						while(rs.next())
						{
							int i = 1;
							while(i < 6) {//attribute개수만큼 수정하기 
								System.out.print(rs.getString(i) + "\t");
								i++;
							}
							System.out.println();
						}
						String memberList = getTeamPlayer(teamName);
						ResultSet ml = null;
						ml = stm.executeQuery(memberList);
						while(ml.next())
						{
							int i = 1;
							while(i < 8) {//attribute개수만큼 수정하기 
								System.out.print(ml.getString(i) + "\t");
								i++;
							}
							System.out.println();
						}						
						break;
					case "비교":
						System.out.println("비교할 커스팀 이름을 입력해주세요!");
						String custom = scan.nextLine();
						String cmp = getTeamRuntime(custom); 
						rs = stm.executeQuery(cmp);
						rs.next();

						String sum = rs.getString(1);
						double value = 0.0;
						value = Double.parseDouble(sum);
						System.out.println("value : " + value);
						
						if(value >= total1RunTime) {
							System.out.println("커스텀팀이 1부 리그를 이길 확률이 더 높습니다.");
						}else if(value >= total2RunTime) {
							System.out.println("커스텀팀이 2부 리그를 이길 확률이 더 높습니다.");
						}else if(value >= total3RunTime) {
							System.out.println("커스텀팀이 3부 리그를 이길 확률이 더 높습니다.");
						}else {
							System.out.println("커스텀팀이 꽤 약하네요...");
						}
						
						break;
					}
					break;
				case "3" :
					System.out.println("원하는 감독의 이름을 입력해주세요!");
					String coach = scan.nextLine();
					String sql = searchCoach(coach);
					rs = stm.executeQuery(sql);
					while(rs.next())
					{
						int i = 1;
						while(i < 6) {
							System.out.print(rs.getString(i) + "\t");
							i++;
						}
						System.out.println();
					}
					
					break;
					
				case "4" :
					System.out.println("원하는 클럽의 이름을 입력해주세요!");
					String club = scan.nextLine();
					System.out.println("이긴경기결과를 보길 원하면 1 을 입력, 진경기 결과를 원하면 2 를 입력, 모든 경기 결과를 원하면 0 을 입력해주세요!");
					String rsSelect = scan.nextLine();
					switch(rsSelect) {
					
						case "0":
							String sql1 = searchWScore(club);
							rs = stm.executeQuery(sql1);
							while(rs.next())
							{
								int i = 2;
								while(i < 6) {
									System.out.print(rs.getString(i) + "\t");
									i++;
								}
								System.out.println();
							}
							sql1 = searchLScore(club);
							rs = stm.executeQuery(sql1);
							while(rs.next())
							{
								int i = 2;
								while(i < 6) {
									System.out.print(rs.getString(i) + "\t");
								i++;
								}
								System.out.println();
							}
							break;
						case "1":
							String sql2 = searchWScore(club);
							rs = stm.executeQuery(sql2);
							while(rs.next())
							{
								int i = 2;
								System.out.println("진팀 : " + rs.getString(3));
								while(i < 6) {

									System.out.print(rs.getString(i) + "\t");
									i++;
								}
								System.out.println();
							}
							break;
						case "2":
							sql1 = searchLScore(club);
							rs = stm.executeQuery(sql1);
							while(rs.next())
							{
								int i = 2;
								while(i < 6) {
									if(i == 2)
									{
										System.out.println("이긴팀 : " + rs.getString(i));
									}
									System.out.print(rs.getString(i) + "\t");
								i++;
								}
								System.out.println();
							}
							break;
					}
					break;
				case "exit":
					System.out.println("종료합니다.");
					return;
				}
			}

			
			
		}catch(Exception e) {
			
		}
		
	}
	
}
