<?php
    include_once 'pt_function.php';
    $db = new DB_Functions();
	$groupid = $_POST["groupid"];
    $users = $db->getUnSyncRowCount3($groupid);
	$a = array();
	$b = array();
    if (mysqli_num_rows($users) > 0){
        
		while ($row = mysqli_fetch_array($users)) {		
			$b["mobileNo"] = $row["mobileNo"];
			$b["groupMember"] = $row["groupMember"];
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