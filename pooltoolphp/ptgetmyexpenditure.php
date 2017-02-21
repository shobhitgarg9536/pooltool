<?php
    include_once 'pt_function.php';
    $db = new DB_Functions();
	$mobileNo = $_POST["mobileNo"];;
	$groupId = $_POST["groupId"];;
    $users = $db->getMyExpenditure($groupId , $mobileNo);
	$a = 0;
	
    if (mysqli_num_rows($users) > 0){
        
		while ($row = mysqli_fetch_array($users)) {		
			
			$a += $row["amount"];
		}
		
	}echo strval($a);
    
?>
