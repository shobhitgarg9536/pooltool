<?php
    include_once 'pt_function.php';
    $db = new DB_Functions();
	$groupid = $_POST["groupid"];
	$myContact = $_POST["myContact"];
    $users = $db->getMyBalanceSheet($groupid , $myContact);
	$a = array();
	$b = array();
    if (mysqli_num_rows($users) > 0){
        
		while ($row = mysqli_fetch_array($users)) {		
			
			$b["amount"] = $row["amount"];
			
			
               
			  $b["item"] = $row["item"];
			  $b["money"] = $row["money"];
			  $b["date"] = $row["date"];
			  $b["time"] = $row["time"];
			  $b["groupMember"] = $row["groupMember"];
			  
			
			array_push($a,$b);
		}
		echo json_encode($a);
	}
    
?>