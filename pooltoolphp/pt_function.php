<?php
        include "ptconnection.php";

class DB_Functions {
		
	 /**
     * Getting all users
     */
    public function getAllUsers() {
		include "ptconnection.php";
        $sql = mysql_query("SELECT * FROM group_detail");
		$result = $conn->query($sql);
        return $result;
    }
	/**
     * Get Yet to Sync row Count
     */
    public function getUnSyncRowCount() {
		include "ptconnection.php";
		$sql = "SELECT * FROM group_detail WHERE syncsts = FALSE";
        $result = $conn->query($sql);
        return $result;
    }
	
	public function getUnSyncRowCount1($mobileno) {
		include "ptconnection.php";
		$sql = "SELECT * FROM group_detail WHERE syncsts = FALSE AND groupMember = $mobileno";
        $result = $conn->query($sql);
        return $result;
    }
	public function getUnSyncRowCount2($groupid) {
		include "ptconnection.php";
		$sql = "SELECT * FROM group_detail WHERE syncsts = FALSE AND groupId = $groupid";
        $result = $conn->query($sql);
        return $result;
    }
	public function getUnSyncRowCount3($groupid) {
		include "ptconnection.php";
		$sql = "SELECT * FROM group_detail WHERE groupId = $groupid";
        $result = $conn->query($sql);
        return $result;
    }
	public function getMyExpenditure($groupId , $mobileNo) {
		include "ptconnection.php";
		$sql = "SELECT * FROM expenditure WHERE groupId = $groupId AND toMobileNo = $mobileNo";
        $result = $conn->query($sql);
        return $result;
    }

	public function getMyBalanceSheet($groupId , $mobileNo) {
		include "ptconnection.php";
		$sql = "SELECT e.* , i.* , g.uid , g.groupMember FROM expenditure e , item_detail i , group_detail g WHERE e.groupId = $groupId AND e.toMobileNo = $mobileNo AND i.itemId = e.itemId AND g.uid = e.uid";
        $result = $conn->query($sql);
        return $result;
    }
	public function updateSyncSts($id, $sts , $groupId){
		include "ptconnection.php";
		$sql = "UPDATE group_detail SET syncsts = '$sts' WHERE groupMember = '$id' AND groupId = '$groupId' ";
		$result = $conn->query($sql);
		return $result;
	}
	public function getGroupId($mobileNo , $date , $time){
		include "ptconnection.php";
		$sql = "SELECT groupId FROM groups WHERE adminMobile = $mobileNo AND date = $date AND time = $time";
        $result = $conn->query($sql);
        return $result;
		
		
	}
	
	
	//generic php function to send FCM push notification
   function sendPushNotificationToFCM($registatoin_ids, $message) {
		//Google cloud messaging GCM-API url
        $url = 'https://android.googleapis.com/gcm/send';
        $fields = array(
            'registration_ids' => $registatoin_ids,
            'data' => $message,
			'priority' => "high",

        );
		// Google Cloud Messaging GCM API Key
		define("GOOGLE_API_KEY", "AIzaSyDA5dlLInMWVsJEUTIHV0u7maB82MCsZbU"); 		
        $headers = array(
            'Authorization: key=' . AIzaSyDA5dlLInMWVsJEUTIHV0u7maB82MCsZbU,
            'Content-Type: application/json'
        );
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
		curl_setopt ($ch, CURLOPT_SSL_VERIFYHOST, 0);	
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
        $result = curl_exec($ch);				
        if ($result === FALSE) {
            die('Curl failed: ' . curl_error($ch));
        }
        curl_close($ch);
        return $result;
    }
	function getfirebaseUID(){
		include "ptconnection.php";
		$sql = "SELECT * FROM register";
		$result = $conn->query($sql);
		return $result;
	}
}
?>