<?php

	if(isset($_POST["token"])){

		$token = $_POST["token"];
		//�����ͺ��̽��� �����ؼ� ��ū�� ����
		include_once 'config.php';
		$conn = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_NAME);
		$query = "INSERT INTO token_info(userid, token) Values (null, '$token') ON DUPLICATE KEY UPDATE token = '$token'; ";
		mysqli_query($conn, $query);

		mysqli_close($conn);
	}
?>