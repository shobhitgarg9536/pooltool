<?php
    include_once 'pt_function.php';
    $db = new DB_Functions();
	$mobile = $_POST["mobileno"];
    $users = $db->getUnSyncRowCount1($mobile);
	$a = array();
	$b = array();
    if (mysqli_num_rows($users) > 0){
        
		while ($row = mysqli_fetch_array($users)) {		
			$b["mobileNo"] = $row["mobileNo"];
			$b["groupName"] = $row["groupName"];
			$b["groupMember"] = $row["groupMember"];
			$b["uid"] = $row["uid"];
			$b["groupId"] = $row["groupId"];
			$b["date"] = $row["date"];
			$b["time"] = $row["time"];
			array_push($a,$b);
		}
		echo json_encode($a);
	}
    /*else{
        $no_of_users = 0;
		$b["count"] = $no_of_users;
		echo json_encode($b);
	}*/
?>