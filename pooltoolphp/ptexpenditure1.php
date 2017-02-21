<?php
    include_once 'pt_function.php';
  
	$moneylist = $_POST["moneyList"];
	$flag =0 ;
	$b = array();
	$data = array();
	$jsonmoneyList = json_decode($moneylist);

    $b = array();
	$allFirebaseUid = array();

	$firebase = $db->getfirebaseUID();
		
		if (mysqli_num_rows($firebase) > 0){
        
		while ($row = mysqli_fetch_array($firebase)) {		
			$b["mobileNo"] = $row["mobileNo"];
			$b["firebaseUid"] = $row["firebaseUid"];
			array_push($allFirebaseUid,$b);
		}
	
	//put foreach loop of json in if loop of firebase so that if first connection is not made then other should also not be made
	foreach ($jsonmoneyList as $jsonvalue){
		
		
		$fromMobileNo = $jsonvalue->fromMobileNo;
		$itemId = $jsonvalue->itemId;
		$toMobileNo = $jsonvalue->toMobileNo;
		$amount = $jsonvalue->amount;
		$groupId = $jsonvalue->groupId;
		
		$mysql_qry = "insert into expenditure(groupId,fromMobileNo,itemId,toMobileNo,amount)values('$groupId','$fromMobileNo','$itemId','$toMobileNo','$amount')";

		if($conn->query($mysql_qry) === TRUE){
	$flag=1;
	if($toMobileNo != $fromMobileNo){
		foreach ($allFirebaseUid as $key => $allFirebaseUid) {
			if($allFirebaseUid['mobileNo'] === groupMember){
				$userfirebaseuid = $allFirebaseUid['firebaseUid'];
				array_push($registrationids , $userfirebaseuid);
					}
				}
         }
}
	}

           $b["fromMobileNo"] = $fromMobileNo;
			$b["itemId"] = $itemId;
			$b["amount"] = $amount;
			$b["groupId"] = $groupId;
			$b["click_action"] = "ViewPagerActivity";
			array_push($data , $b);
	
         $users = $db->sendPushNotificationToFCM($registrationids , $data);
	if($flag == 1){
		echo "item inserted successfully";
	}
	else{
		echo "Error:".$mysql_qry."<br>".$conn->error;
	}

$conn->close();
}else{
	echo "Item could not be insert successfully";
}
?>