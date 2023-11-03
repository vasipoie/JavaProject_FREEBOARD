package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import print.Print;
import service.FreeBoardService;
import util.ScanUtil;
import util.View;
import vo.FreeBoard;

public class Controller extends Print {
	// 세션
	static public Map<String, Object> sessionStorage = new HashMap<>();

	FreeBoardService boardService = FreeBoardService.getInstance();

	public static void main(String[] args) {
//		Controller c = new Controller();
//		c.start();
		new Controller().start();
	}

	 void start() {
		View view = View.HOME;
		while (true) {
			switch (view) {
			case HOME:
				view = home();
				break;
			case BOARD_LIST:	//전체 게시판 조회
				view = boardList();
				break;
			case BOARD_DETAIL:	//상세 페이지 조회
				view = boardView();
				break;
			case BOARD_INSERT:	//게시판 신규 등록
				view = boardInsert();
				break;
			case BOARD_DELETE:	//게시판 삭제
				view = boardDelete();
				break;
			case BOARD_UPDATE:	//게시판 수정
				view = boardUpdate();
				break;
			}
		}
	}
	
	//게시판 수정 BOARD_UPDATE
	 View boardUpdate() {
		int board_no = (int) sessionStorage.get("board_no");
		String name = ScanUtil.nextLine("이름");
		String writer = ScanUtil.nextLine("작성자");
		String content = ScanUtil.nextLine("내용");
		
		List<Object> param = new ArrayList<Object>();
		param.add(name);
		param.add(writer);
		param.add(content);
		param.add(board_no);
		
		boardService.boardUpdate(param);
		
		return View.BOARD_LIST;
	}

	//게시판 삭제 BOARD_DELETE
	 View boardDelete() {
		int board_no = (int) sessionStorage.get("board_no");
		String del = ScanUtil.nextLine(board_no+". 게시글을 정말 삭제하시겠습니까?(y/n)");
		if(del.equals("y")) {
			boardService.boardDelete(board_no);
		}//y,n이외의 것이 입력되면 다시 돌아가게
//		else
		return View.BOARD_LIST;
	}

	//게시판 신규 등록 BOARD_INSERT
	 View boardInsert() {
		/* 게시번호 	제목	작성자	작성시간	내용(10자만 출력)
		 * 3		게시글3		테스트1	2023-11-01	오늘 날씨는 ....
		 * 2		게시글2		테스트1 2023-11-01	오늘 날씨는 ....
		 * 1		게시글1		테스트1	2023-10-31	오늘 날씨는 ....
		 */
		String name = ScanUtil.nextLine("제목을 작성하세요.");
		String writer = ScanUtil.nextLine("작성자를 작성하세요");
		String content = ScanUtil.nextLine("내용을 입력하세요.");
		
		/*
		 * name, writer다 쓰기 귀찮으니 list에 담아서 보낸다
		 */
		List<Object> param = new ArrayList<Object>();
		param.add(name);
		param.add(writer);
		param.add(content);
		
		/*
		 * 입력받은 내용을 서비스로 보냄
		 */
		 boardService.boardInsert(param);
		 return View.BOARD_LIST;
	}

	//상세페이지 조회 BOARD_DETAIL
	 View boardView() {
		int board_no = ScanUtil.nextInt("게시판을 선택하세요.");
		/*
		 * 세션스토리지에 선택한 게시판 번호를 넣음
		 * 위에 삭제할때 꺼냄
		 */
		sessionStorage.put("board_no", board_no);
		FreeBoard fb = boardService.boardView(board_no);
		printView(fb);
		int select = ScanUtil.nextInt("메뉴를 선택하세요.");
		switch (select) {
		case 1:
			return View.BOARD_UPDATE;
		case 2:
			return View.BOARD_DELETE;
		default :
			return View.BOARD_DETAIL;
		}
	}

	//전체게시판 조회 BOARD_LIST
	 View boardList() {
		List<FreeBoard> list = boardService.boardList();
		printList(list);
		System.out.println("----------------------------------");
		int select = ScanUtil.nextInt("상세페이지를 조회하고 싶으시면 1번을 선택하세요.");
		System.out.println("----------------------------------");
		switch (select) {
		case 1:
			return View.BOARD_DETAIL;
		case 2:
			return View.HOME;
		default :
			return View.BOARD_LIST;
		}
	}

	//전체게시판 조회, 게시판 신규 등록 메뉴 보임
	 View home() {
		printHome();
		int select = ScanUtil.nextInt("메뉴를 선택하세요.");
		switch (select) {
		case 1:
			return View.BOARD_LIST;
		case 2:
			return View.BOARD_INSERT;
		default :
			return View.HOME;
		}
	}
}
