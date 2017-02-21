package com.shobhit.pooltool;

/**
 * Created by Shobhit-pc on 12/24/2016.
 */

public class UrlList {
    //to register user
    public static final String register_url="http://pool1.herokuapp.com/ptregister.php";
    //to send group_accept_or_not_json_file
    public static final String group_accept_or_not_json_file="http://10.0.2.2/";
    //to get get_my_expenditure
    public static final String get_my_expenditure="http://pool1.herokuapp.com/ptgetmyexpenditure.php";
    //to get json array of pooltool active users contact list
    public static final String get_contact_list="http://pool1.herokuapp.com/ptcontactlist.php";
    //to create new account
    public static final String create_group="http://pool1.herokuapp.com/ptcreategroup.php";
    //to send new item add from AddExpense.class
    public static final String add_expense="http://pool1.herokuapp.com/ptadditem.php";
    //to add expenditure from AddExpense.class
    public static final String add_expenditure="http://pool1.herokuapp.com/ptexpenditure1.php";
    //to get the balance sheet
    public static final String my_balance_sheet="http://pool1.herokuapp.com/ptmybalancesheet.php";
    //to update firebase uid
    public static final String update_firebase_uid="http://pool1.herokuapp.com/ptupdate_firebaseuid.php";
    //to get the list of group Member acc to group
    public static final String group_member_list="http://pool1.herokuapp.com/ptgroupmemberList.php";
    //to get the list of settle amount of each member in a group
    public static final String settle_up="http://pool1.herokuapp.com/ptsettleup.php";
    //to get the list of settle amount of each member in a group
    public static final String settle_up_pay_amount="http://pool1.herokuapp.com/ptsettleuppay.php";
    //to get the list of settle amount of each member in a group
    public static final String settle_up_pay_amount_request="http://pool1.herokuapp.com/ptsettleuppayrequest.php";
    //to change group member admin state by admin
    public static final String manage_group_admin_state="http://pool1.herokuapp.com/ptmanagegroupadminstate.php";
}
