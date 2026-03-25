package com.dy.colony.mvp.model.entity;

import java.io.Serializable;

/**
 * @author luoyl
 * @desc
 * @date 2026/3/24
 */
public class ObjUserData implements Serializable {


    private String rights;
    private UserDTO user;
    private String token;

    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static class UserDTO {
        private String id;
        private String workers_id;
        private String user_name;
        private String password;
        private String realname;
        private String role_id;
        private int status;
        private int sorting;
        private String create_by;
        private String create_date;
        private String update_by;
        private String update_date;
        private int delete_flag;
        private int login_count;
        private String login_time;
        private int depart_id;
        private int point_id;
        private String reg_id;
        private String remark;
        private String signature_file;
        private String param1;
        private String param2;
        private String param3;
        private String wx_openid;
        private String wx_nickname;
        private String wx_image;
        private String bind_time;
        private String relieve_time;
        private String phone;
        private int d_id;
        private String d_depart_name;
        private String d_description;
        private int d_depart_pid;
        private String d_project_id;
        private String d_principal_id;
        private String d_region_id;
        private String d_depart_code;
        private String d_depart_type;
        private String d_mobile_phone;
        private String d_fax;
        private String d_address;
        private int d_sorting;
        private int d_delete_flag;
        private String d_is_outsourcing;
        private String d_create_by;
        private String d_create_date;
        private String d_update_by;
        private String d_update_date;
        private int p_id;
        private String p_point_name;
        private String p_point_code;
        private String p_point_type;
        private int p_depart_id;
        private String p_region_id;
        private String p_manager_id;
        private String p_license_plate;
        private String p_phone;
        private String p_address;
        private String p_place_x;
        private String p_place_y;
        private String p_remark;
        private String p_IMEI;
        private int p_sorting;
        private int p_delete_flag;
        private String p_create_by;
        private String p_create_date;
        private String p_update_by;
        private String p_update_date;
        private String d_departs_id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getWorkers_id() {
            return workers_id;
        }

        public void setWorkers_id(String workers_id) {
            this.workers_id = workers_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public String getRole_id() {
            return role_id;
        }

        public void setRole_id(String role_id) {
            this.role_id = role_id;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getSorting() {
            return sorting;
        }

        public void setSorting(int sorting) {
            this.sorting = sorting;
        }

        public String getCreate_by() {
            return create_by;
        }

        public void setCreate_by(String create_by) {
            this.create_by = create_by;
        }

        public String getCreate_date() {
            return create_date;
        }

        public void setCreate_date(String create_date) {
            this.create_date = create_date;
        }

        public String getUpdate_by() {
            return update_by;
        }

        public void setUpdate_by(String update_by) {
            this.update_by = update_by;
        }

        public String getUpdate_date() {
            return update_date;
        }

        public void setUpdate_date(String update_date) {
            this.update_date = update_date;
        }

        public int getDelete_flag() {
            return delete_flag;
        }

        public void setDelete_flag(int delete_flag) {
            this.delete_flag = delete_flag;
        }

        public int getLogin_count() {
            return login_count;
        }

        public void setLogin_count(int login_count) {
            this.login_count = login_count;
        }

        public String getLogin_time() {
            return login_time;
        }

        public void setLogin_time(String login_time) {
            this.login_time = login_time;
        }

        public int getDepart_id() {
            return depart_id;
        }

        public void setDepart_id(int depart_id) {
            this.depart_id = depart_id;
        }

        public int getPoint_id() {
            return point_id;
        }

        public void setPoint_id(int point_id) {
            this.point_id = point_id;
        }

        public String getReg_id() {
            return reg_id;
        }

        public void setReg_id(String reg_id) {
            this.reg_id = reg_id;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getSignature_file() {
            return signature_file;
        }

        public void setSignature_file(String signature_file) {
            this.signature_file = signature_file;
        }

        public String getParam1() {
            return param1;
        }

        public void setParam1(String param1) {
            this.param1 = param1;
        }

        public String getParam2() {
            return param2;
        }

        public void setParam2(String param2) {
            this.param2 = param2;
        }

        public String getParam3() {
            return param3;
        }

        public void setParam3(String param3) {
            this.param3 = param3;
        }

        public String getWx_openid() {
            return wx_openid;
        }

        public void setWx_openid(String wx_openid) {
            this.wx_openid = wx_openid;
        }

        public String getWx_nickname() {
            return wx_nickname;
        }

        public void setWx_nickname(String wx_nickname) {
            this.wx_nickname = wx_nickname;
        }

        public String getWx_image() {
            return wx_image;
        }

        public void setWx_image(String wx_image) {
            this.wx_image = wx_image;
        }

        public String getBind_time() {
            return bind_time;
        }

        public void setBind_time(String bind_time) {
            this.bind_time = bind_time;
        }

        public String getRelieve_time() {
            return relieve_time;
        }

        public void setRelieve_time(String relieve_time) {
            this.relieve_time = relieve_time;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getD_id() {
            return d_id;
        }

        public void setD_id(int d_id) {
            this.d_id = d_id;
        }

        public String getD_depart_name() {
            return d_depart_name;
        }

        public void setD_depart_name(String d_depart_name) {
            this.d_depart_name = d_depart_name;
        }

        public String getD_description() {
            return d_description;
        }

        public void setD_description(String d_description) {
            this.d_description = d_description;
        }

        public int getD_depart_pid() {
            return d_depart_pid;
        }

        public void setD_depart_pid(int d_depart_pid) {
            this.d_depart_pid = d_depart_pid;
        }

        public String getD_project_id() {
            return d_project_id;
        }

        public void setD_project_id(String d_project_id) {
            this.d_project_id = d_project_id;
        }

        public String getD_principal_id() {
            return d_principal_id;
        }

        public void setD_principal_id(String d_principal_id) {
            this.d_principal_id = d_principal_id;
        }

        public String getD_region_id() {
            return d_region_id;
        }

        public void setD_region_id(String d_region_id) {
            this.d_region_id = d_region_id;
        }

        public String getD_depart_code() {
            return d_depart_code;
        }

        public void setD_depart_code(String d_depart_code) {
            this.d_depart_code = d_depart_code;
        }

        public String getD_depart_type() {
            return d_depart_type;
        }

        public void setD_depart_type(String d_depart_type) {
            this.d_depart_type = d_depart_type;
        }

        public String getD_mobile_phone() {
            return d_mobile_phone;
        }

        public void setD_mobile_phone(String d_mobile_phone) {
            this.d_mobile_phone = d_mobile_phone;
        }

        public String getD_fax() {
            return d_fax;
        }

        public void setD_fax(String d_fax) {
            this.d_fax = d_fax;
        }

        public String getD_address() {
            return d_address;
        }

        public void setD_address(String d_address) {
            this.d_address = d_address;
        }

        public int getD_sorting() {
            return d_sorting;
        }

        public void setD_sorting(int d_sorting) {
            this.d_sorting = d_sorting;
        }

        public int getD_delete_flag() {
            return d_delete_flag;
        }

        public void setD_delete_flag(int d_delete_flag) {
            this.d_delete_flag = d_delete_flag;
        }

        public String getD_is_outsourcing() {
            return d_is_outsourcing;
        }

        public void setD_is_outsourcing(String d_is_outsourcing) {
            this.d_is_outsourcing = d_is_outsourcing;
        }

        public String getD_create_by() {
            return d_create_by;
        }

        public void setD_create_by(String d_create_by) {
            this.d_create_by = d_create_by;
        }

        public String getD_create_date() {
            return d_create_date;
        }

        public void setD_create_date(String d_create_date) {
            this.d_create_date = d_create_date;
        }

        public String getD_update_by() {
            return d_update_by;
        }

        public void setD_update_by(String d_update_by) {
            this.d_update_by = d_update_by;
        }

        public String getD_update_date() {
            return d_update_date;
        }

        public void setD_update_date(String d_update_date) {
            this.d_update_date = d_update_date;
        }

        public int getP_id() {
            return p_id;
        }

        public void setP_id(int p_id) {
            this.p_id = p_id;
        }

        public String getP_point_name() {
            return p_point_name;
        }

        public void setP_point_name(String p_point_name) {
            this.p_point_name = p_point_name;
        }

        public String getP_point_code() {
            return p_point_code;
        }

        public void setP_point_code(String p_point_code) {
            this.p_point_code = p_point_code;
        }

        public String getP_point_type() {
            return p_point_type;
        }

        public void setP_point_type(String p_point_type) {
            this.p_point_type = p_point_type;
        }

        public int getP_depart_id() {
            return p_depart_id;
        }

        public void setP_depart_id(int p_depart_id) {
            this.p_depart_id = p_depart_id;
        }

        public String getP_region_id() {
            return p_region_id;
        }

        public void setP_region_id(String p_region_id) {
            this.p_region_id = p_region_id;
        }

        public String getP_manager_id() {
            return p_manager_id;
        }

        public void setP_manager_id(String p_manager_id) {
            this.p_manager_id = p_manager_id;
        }

        public String getP_license_plate() {
            return p_license_plate;
        }

        public void setP_license_plate(String p_license_plate) {
            this.p_license_plate = p_license_plate;
        }

        public String getP_phone() {
            return p_phone;
        }

        public void setP_phone(String p_phone) {
            this.p_phone = p_phone;
        }

        public String getP_address() {
            return p_address;
        }

        public void setP_address(String p_address) {
            this.p_address = p_address;
        }

        public String getP_place_x() {
            return p_place_x;
        }

        public void setP_place_x(String p_place_x) {
            this.p_place_x = p_place_x;
        }

        public String getP_place_y() {
            return p_place_y;
        }

        public void setP_place_y(String p_place_y) {
            this.p_place_y = p_place_y;
        }

        public String getP_remark() {
            return p_remark;
        }

        public void setP_remark(String p_remark) {
            this.p_remark = p_remark;
        }

        public String getP_IMEI() {
            return p_IMEI;
        }

        public void setP_IMEI(String p_IMEI) {
            this.p_IMEI = p_IMEI;
        }

        public int getP_sorting() {
            return p_sorting;
        }

        public void setP_sorting(int p_sorting) {
            this.p_sorting = p_sorting;
        }

        public int getP_delete_flag() {
            return p_delete_flag;
        }

        public void setP_delete_flag(int p_delete_flag) {
            this.p_delete_flag = p_delete_flag;
        }

        public String getP_create_by() {
            return p_create_by;
        }

        public void setP_create_by(String p_create_by) {
            this.p_create_by = p_create_by;
        }

        public String getP_create_date() {
            return p_create_date;
        }

        public void setP_create_date(String p_create_date) {
            this.p_create_date = p_create_date;
        }

        public String getP_update_by() {
            return p_update_by;
        }

        public void setP_update_by(String p_update_by) {
            this.p_update_by = p_update_by;
        }

        public String getP_update_date() {
            return p_update_date;
        }

        public void setP_update_date(String p_update_date) {
            this.p_update_date = p_update_date;
        }

        public String getD_departs_id() {
            return d_departs_id;
        }

        public void setD_departs_id(String d_departs_id) {
            this.d_departs_id = d_departs_id;
        }
    }
}
