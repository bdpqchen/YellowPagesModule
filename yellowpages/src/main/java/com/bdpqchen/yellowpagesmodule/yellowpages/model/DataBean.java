package com.bdpqchen.yellowpagesmodule.yellowpages.model;

import java.util.List;

/**
 * Created by chen on 17-2-26.
 */

public class DataBean {

    private List<DataBeanList> data;

    public List<DataBeanList> getData() {
        return data;
    }

    public void setData(List<DataBeanList> data) {
        this.data = data;
    }

    public static class DataBeanList {
        /**
         * category_name : 校级
         * category_list : [{"department":[{"department_name":"宣传部","department_list":[{"item_name":"宣传部校报","item_phone":"27403289"},{"item_name":"宣传部广播站","item_phone":"27406475"}]}]},{"department":[{"department_name":"学工部","department_list":[{"item_name":"学工部资助管理中心","item_phone":"27407083"},{"item_name":"学工部本科生教育科","item_phone":"27403407"}]}]}]
         */

        private String category_name;
        private List<CategoryListBean> category_list;

        public String getCategory_name() {
            return category_name;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
        }

        public List<CategoryListBean> getCategory_list() {
            return category_list;
        }

        public void setCategory_list(List<CategoryListBean> category_list) {
            this.category_list = category_list;
        }

        public static class CategoryListBean {
            private List<DepartmentBean> department;

            public List<DepartmentBean> getDepartment() {
                return department;
            }

            public void setDepartment(List<DepartmentBean> department) {
                this.department = department;
            }

            public static class DepartmentBean {
                /**
                 * department_name : 宣传部
                 * department_list : [{"item_name":"宣传部校报","item_phone":"27403289"},{"item_name":"宣传部广播站","item_phone":"27406475"}]
                 */

                private String department_name;
                private List<DepartmentListBean> department_list;

                public String getDepartment_name() {
                    return department_name;
                }

                public void setDepartment_name(String department_name) {
                    this.department_name = department_name;
                }

                public List<DepartmentListBean> getDepartment_list() {
                    return department_list;
                }

                public void setDepartment_list(List<DepartmentListBean> department_list) {
                    this.department_list = department_list;
                }

                public static class DepartmentListBean {
                    /**
                     * item_name : 宣传部校报
                     * item_phone : 27403289
                     */

                    private String item_name;
                    private String item_phone;

                    public String getItem_name() {
                        return item_name;
                    }

                    public void setItem_name(String item_name) {
                        this.item_name = item_name;
                    }

                    public String getItem_phone() {
                        return item_phone;
                    }

                    public void setItem_phone(String item_phone) {
                        this.item_phone = item_phone;
                    }
                }
            }
        }
    }
}
