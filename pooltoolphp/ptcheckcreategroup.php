<?php
    require 'ptconnection.php';
    include_once 'pt_function.php';
	$db= new DB_Functions();
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
	
	}

		foreach ($allFirebaseUid as $key => $allFirebaseUid) {
			if($allFirebaseUid['mobileNo'] === '9536331750'){
				$userfirebaseuid = $allFirebaseUid['firebaseUid'];
			}
					}
					echo $userfirebaseuid;
				
		
         
         
	
	
	        
$conn->close();
?>
