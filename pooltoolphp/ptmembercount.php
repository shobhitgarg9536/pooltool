<?php
    include_once 'pt_function.php';
    $db = new DB_Functions();
	$groupid = $_POST["groupid"];
    $users = $db->getUnSyncRowCount2($groupid);
	$a = array();
	$b = array();
    if ($users != false){
        $no_of_users = mysqli_num_rows($users);		
		$b["count"] = $no_of_users;
		echo json_encode($b);
	}
    else{
        $no_of_users = 0;
		$b["count"] = $no_of_users;
		echo json_encode($b);
	}
?>