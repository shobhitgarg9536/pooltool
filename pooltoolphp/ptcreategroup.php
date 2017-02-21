<?php
    require 'ptconnection.php';
    include_once 'pt_function.php';
	$db= new DB_Functions();
	$creategrouplist = $_POST["create_group_list"];
	$flag =0 ;
	$jsonmoneyList = json_decode($creategrouplist);
	$registrationids = array();
	$data = array();
	$b = array();
	$allFirebaseUid = array();

	$firebase = $db->getfirebaseUID();
		
		if (mysqli_num_rows($firebase) > 0){
        
		while ($row = mysqli_fetch_array($firebase)) {		
			$b["mobileNo"] = $row["mobileNo"];
			$b["firebaseUid"] = $row["firebaseUid"];
			array_push($allFirebaseUid,$b);
		}
	
	
	foreach ($jsonmoneyList as $jsonvalue){
		
		
		$mobileNo = $jsonvalue->mobileNo;
		$groupName = $jsonvalue->groupName;
		$groupMember = $jsonvalue->groupMember;
		$groupId = $jsonvalue->groupId;
		$date = $jsonvalue->date;
		$time = $jsonvalue->time;
		$admin = $jsonvalue->admin;
		$mysql_qry = "insert into group_detail(mobileNo,groupName,groupMember,groupId,date,time,admin)values('$mobileNo','$groupName','$groupMember','$groupId','$date','$time','$admin')";

		if($conn->query($mysql_qry) === TRUE){
	    $flag=1;

	    if($groupMember != $mobileNo){
		foreach ($allFirebaseUid as $key => $allFirebaseUid) {
			if($allFirebaseUid['mobileNo'] === groupMember){
				$userfirebaseuid = $allFirebaseUid['firebaseUid'];
				array_push($registrationids , $userfirebaseuid);
					}
				}

         }
     }
         else{
         	$flag = 0;
         }
	
	}
	        $b["mobileNo"] = $mobileNo;
			$b["groupName"] = $groupName;
			$b["groupId"] = $groupId;
			$b["date"] = $date;
			$b["time"] = $time;
			$b["click_action"] = "ViewPagerActivity";
			array_push($data , $b);
	
         $users = $db->sendPushNotificationToFCM($registrationids , $data);
	if($flag == 1){
		echo "Group Successfully created";
	}
	else{
		echo "Error:".$mysql_qry."<br>".$conn->error;
	}
$conn->close();
}else{
	echo "Group could not created successfully";
}
?>
