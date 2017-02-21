<?php
    include_once 'pt_function.php';
    $db = new DB_Functions();
	$groupMember = $_POST["groupMember"];
    $users = $db->getUnSyncRowCount1($groupMember);
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