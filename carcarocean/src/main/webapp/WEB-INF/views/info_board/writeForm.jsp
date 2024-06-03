<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>정보공유 게시판 글쓰기</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
<script type="text/javascript">
window.onload = function(){
	const myForm = document.getElementById('write_form');
	
	myForm.onsubmit = function(){
		const title = document.getElementById('info_board_title');
		if(title.value.trim() == ''){
			alert('제목을 입력하세요.');
			title.value = '';
			title.focus();
			return false;
		}
		const content = document.getElementById('info_board_content');
		if(content.value.trim() == ''){
			alert('내용을 입력하세요.');
			content.value = '';
			content.focus();
			return false;
		}
	};
};
</script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<h2>글쓰기</h2>
		<form id="write_form" action="write.do" method="post" enctype="multipart/form-data">
			<ul>
				<li>
					<label for="info_board_title">제목</label>
					<input type="text" name="info_board_title" id="info_board_title" maxlength="50">
				</li>
				<li>
					<label for="info_board_content">내용</label>
					<textarea rows="5" cols="40" name="info_board_content" id="info_board_content"></textarea>
				</li>
				<li>
					<label for="info_board_photo">사진</label>
					<input type="file" name="info_board_photo" id="info_board_photo" accept="image/gif, image/png, image/jpeg" multiple>
				</li>
			</ul>
			<div class="align-center">
				<input type="submit" value="글쓰기">
				<input type="button" value="목록" onclick="location.href='list.do'">
			</div>
		</form>
	</div>
</div>
</body>
</html>