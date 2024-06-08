package kr.webSocket;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;

//WebSocket의 호스트 주소 설정
@ServerEndpoint("/websocket")
public class WebSocket {
	// WebSocket으로 브라우저가 접속하면 요청되는 함수
	@OnOpen
	public void handleOpen() {
		// 콘솔에 접속 로그를 출력한다.    
//		System.out.println("client is now connected...");
	}
	
	// WebSocket으로 메시지가 오면 요청되는 함수
	@OnMessage
	public String handleMessage(String message) {
		// 메시지 내용을 콘솔에 출력한다.
//		System.out.println("receive from client : " + message);
		// 에코 메시지를 작성한다.
//		String replymessage = "echo " + message;
		// 에코 메시지를 콘솔에 출력한다.
//		System.out.println("send to client : " + replymessage);
		// 에코 메시지를 브라우저에 보낸다.
//		return replymessage;
		String answer = "";
		if(message.equals("1")) {
			answer = "온라인으로 구매하셨을 경우 로그인 후 마이페이지를 확인하세요. 오프라인 구매는 차량을 구매하신 매장 또는 차량평가사에게 문의해주세요.";
		} else if(message.equals("2")) {
			answer = "마이페이지 => 거래내역";
		} else if(message.equals("3")) {
			answer = "메뉴들 대충 링크걸어서 넣기";
		} else if(message.equals("4")) {
			answer = "카카오톡 채팅 상담시간 09:00~17:40 (평일, 점심시간 11:40~13:00)";
		} else {
			answer = "1~4 메뉴를 선택해주세요";
		}
		return answer;
	}
	
	// WebSocket과 브라우저가 접속이 끊기면 요청되는 함수
	@OnClose
	public void handleClose() {
		// 콘솔에 접속 끊김 로그를 출력한다.
//		System.out.println("client is now disconnected...");
	}
	
	// WebSocket과 브라우저 간에 통신 에러가 발생하면 요청되는 함수.
	@OnError
	public void handleError(Throwable t) {
		// 콘솔에 에러를 표시한다.
//		t.printStackTrace();
	}
}