<?php
	header('Content-Type: application/json;charset=utf-8');
	
	require_once('../users/session.php');

	require_once('../users/dbcon.php');
	
	error_reporting(0);
		
	// @ 앞에 붙이면 waring 출력하지 않음
	//$dbc = @mysqli_connect($dbhost, $dbuser, $dbpass, $dbname);
	$dbc = mysqli_connect($dbhost, $dbuser, $dbpass, $dbname);
	if ($dbc == null) {
		$response->error = "Connection Error";
		exit(json_encode($response));
	}
	
	$post = json_decode(file_get_contents('php://input'),true);
	if ($post == null) {
		// 로그인 페이지를 통해 로그인
		if (empty($_POST['c_name']) || empty($_POST['user_email']) || empty($_POST['content']) || empty($_POST['rating']) || empty($_POST['star'])) {
			$response->error = "Input Error";
			exit(json_encode($response));
		}
		
		$c_name = mysqli_real_escape_string($dbc, trim($_POST['c_name']));
		$user_email = mysqli_real_escape_string($dbc, trim($_POST['user_email']));
		$content = mysqli_real_escape_string($dbc, trim($_POST['content']));
		$rating = mysqli_real_escape_string($dbc, trim($_POST['rating']));
		$star = mysqli_real_escape_string($dbc, trim($_POST['star']));
	}
	else { 	// JSON Object
		$c_name = $post['c_name'];
		$user_email  = $post['user_email'];
		$content  = $post['content'];
		$rating = $post['rating'];
		$star = $post['star'];
	}
	
	// TODO: 체크 안해도 되는지 확인 필요 (보안 문제 발생 가능)
	//       같은 값으로 확인되는데 에러 발생해서 주석 처리함
	// if ($_SESSION['id'] != $id) {
		// $response->error = "User Error" ;
		// exit(json_encode($response));
	// }

	mysqli_query($dbc, "set names utf8;");

	
	// insert -----------------------------------------------------------------
	$query = "insert into review_info values " .
			"(null, '$c_name', '$user_email', '$content', '$rating')";
	//echo $query; 
	$result = mysqli_query($dbc, $query);

	if ($result == null) {
		$response->error = "Insertion Error";
		exit(json_encode($response));
	}	

	$result = mysqli_query($dbc, "UPDATE place_info SET star='$star' WHERE name = '$c_name'");

	$response->status = "Success";
	exit(json_encode($response));

	
?>

