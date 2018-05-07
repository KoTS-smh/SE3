<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-menu"></i> 任务</el-breadcrumb-item>
                <el-breadcrumb-item>接取任务列表</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="handle-box">
            <el-select v-model="select_cate" placeholder="筛选任务" class="handle-select mr10" @change="handleChange()">
                <el-option key="1" label="已完成" value="已完成"></el-option>
                <el-option key="2" label="未完成" value="未完成"></el-option>
                <el-option key="3" label="草稿" value="草稿"></el-option>
            </el-select>
            <el-button type="primary"  @click="newTask">接取任务</el-button>
        </div>
        <el-table :data="tableData"  style="width: 100%" ref="multipleTable" @selection-change="handleSelectionChange">
            <el-table-column prop="taskName" label="任务名称" width="140"></el-table-column>
            <el-table-column prop="taskId" label="任务Id" width="140"></el-table-column>
            <el-table-column prop="submited" label="任务状态" width="140"></el-table-column>
            <el-table-column prop="degreeOfCompletion" label="完成度" width="140"></el-table-column>
            <el-table-column prop="endDate" label="截止时间" width="210"></el-table-column>
            <el-table-column prop="rate" label="评分" width="140"></el-table-column>

            <el-table-column label="操作" width="200">
                <template slot-scope="scope">
                    <el-button size="small"
                            @click="handleEdit(scope.$index, scope.row)">详情</el-button>
                    <el-button size="small" type="danger"
                            @click="handleDelete(scope.$index, scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-dialog title="任务详情" :visible.sync="dialogVisible">
            <el-form :model="selectTable">
                <el-form-item label="任务ID" label-width="100px">
                    <el-input v-model="selectTable.taskId" auto-complete="off" readonly="true"></el-input>
                </el-form-item>
                <el-form-item label="截止时间" label-width="100px">
                    <el-input v-model="selectTable.endDate" auto-complete="off" readonly="true"></el-input>

                </el-form-item>
                <el-form-item label="状态" label-width="100px">
                    <el-input v-model="selectTable.submited" auto-complete="off" readonly="true"></el-input>
                </el-form-item>

            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveChange()">确 定</el-button>
                <el-button type="success" @click="startTag()">开始标注</el-button>
            </div>
        </el-dialog>
    </div>


</template>

<script>
    import axios from 'axios';
    import VueCoreImageUpload  from 'vue-core-image-upload';
    export default {
        data() {
            return {
                tableData: [],
                cur_page: 1,
                multipleSelection: [],
                select_cate: '',
                select_word: '',
                del_list: [],
                selectTable: {},
                dialogVisible: false,
                newDialogVisible: false,
                is_search: false,
                src: '',
                fileList: []
            }
        },
        computed: {
            data(){
                const self = this;
                return self.tableData.filter(function(d){
                    let is_del = false;
                    for (let i = 0; i < self.del_list.length; i++) {
                        if(d.name === self.del_list[i].name){
                            is_del = true;
                            break;
                        }
                    }
                    if(!is_del){
                        if(d.address.indexOf(self.select_cate) > -1 &&
                            (d.name.indexOf(self.select_word) > -1 ||
                            d.address.indexOf(self.select_word) > -1)
                        ){
                            return d;
                        }
                    }
                })
            }
        },
        components:{
            VueCoreImageUpload
        },
        methods: {
            handleCurrentChange(val){
                this.cur_page = val;
                this.getData();
            },
            search(){
                this.is_search = true;
            },
            formatter(row, column) {
                return row.address;
            },
            filterTag(value, row) {
                return row.tag === value;
            },
            handleEdit(index, row) {
                this.$router.push({
                    path:'/checkTask',
                    query:{
                        taskId:row.taskId
                    }
                })
            },
            handleDelete(index, row) {
                if(row.submited === true){
                    this.$message.info("完成的任务无法删除！")
                }else {
                    this.$confirm('此操作将移除该标注任务, 是否继续?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        axios.get('http://localhost:8080/taskOrder/delete', {
                            params: {
                                taskOrderId: row.taskOrderId,
                                userId: localStorage.getItem("userId")
                            }
                        }).then((response) => {
                            if (response.data.code !== 0) {
                                this.$message({
                                    type: 'error',
                                    message: '移除失败!'
                                });
                            } else {
                                this.$message({
                                    type: 'success',
                                    message: '移除成功!'
                                });

                                this.placeTheData();
                            }
                        }).catch(() => {
                            this.$message({
                                type: 'error',
                                message: '网络异常!'
                            });
                        })
                    }).catch(() => {
                        this.$message({
                            type: 'error',
                            message: '已取消！'
                        });
                    });
                }
            },
            handleChange() {
                var selection = this.select_cate;
                var userId = localStorage.getItem("userId");
                var mydata;
                if(selection === "已完成"){
                    axios.post("http://localhost:8080/taskOrder/getAllSubmited", {"userId": userId, "password": ""})
                    .then(response => {
                        console.log(response);
                        mydata = JSON.parse(response.data.data);
                        var i = 0;
                        for(i = 0;i < mydata.length;++i){
                            mydata[i].endDate = this.convertDate(mydata[i].endDate);
                            mydata[i].beginDate = this.convertDate(mydata[i].beginDate);
                            var tmprate = mydata[i].rate;
                            if(tmprate != null && tmprate !== undefined && tmprate !== -1){
                                //donothing
                            }else{
                                mydata[i].rate = '未评分';
                            }
                        }
                        this.tableData = mydata;
                    })
                }else if(selection === "未完成"){
                    axios.post("http://localhost:8080/taskOrder/getAllunSubmited", {"userId": userId, "password": ""})
                    .then(response => {
                        mydata = JSON.parse(response.data.data);
                        var i = 0;
                        for(i = 0;i < mydata.length;++i){
                            mydata[i].endDate = this.convertDate(mydata[i].endDate);
                            mydata[i].beginDate = this.convertDate(mydata[i].beginDate);
                            var tmprate = mydata[i].rate;
                            if(tmprate != null && tmprate !== undefined && tmprate !== -1){
                                //doNothing
                            }else{
                                mydata[i].rate = '未评分';
                            }
                        }
                        this.tableData = mydata;
                    })
                }
            },
            delAll(){
                const self = this,
                    length = self.multipleSelection.length;
                let str = '';
                self.del_list = self.del_list.concat(self.multipleSelection);
                for (let i = 0; i < length; i++) {
                    str += self.multipleSelection[i].name + ' ';
                }
                self.$message.error('删除了'+str);
                self.multipleSelection = [];
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            saveChange() {
                //向后端传送数据
                this.tableData[0].endDate = this.selectTable.endDate;
                this.dialogVisible = false
            },
            startTag(val) {

            },
            imageuploaded(res) {
                console.log(res)
            },
            handleError(){
                this.$notify.error({
                    title: '上传失败',
                    message: '图片上传接口上传失败，可更改为自己的服务器接口'
                });
            },
            newTask(){
                this.$router.push("/homepage")
            },

            placeTheData() {
                var username = localStorage.getItem('username');
                var userId = localStorage.getItem('userId');
                axios.post('http://localhost:8080/taskOrder/getAll', {"username": username, "password": '', "userId": userId})
                .then(response => {
                    for(let j = 0;j < response.data.data.length; j++){
                        if(response.data.data[j].submited === false){
                            response.data.data[j].submited = "未提交";
                        }else{
                            response.data.data[j].submited = "已提交";
                        }

                        response.data.data[j].endDate = this.convertDate(response.data.data[j].endDate);
                        var tmprate = response.data.data[j].rate;
                        if(tmprate != null && tmprate !== undefined && tmprate !== -1){
                            //donothing
                        }else{
                            response.data.data[j].rate = '未评分';
                        }
                    }
                    var list = response.data.data;
                    this.tableData = list;

                }

                ).catch(err => {
                    console.log(err)
                })

            },
            convertDate(indate) {
                var date = new Date(indate);
                var year = date.getFullYear() + '-';
                var month = (date.getMonth() + 1) + '-';
                var day = date.getDate() - 1;

                var hour = date.getUTCHours();

                hour = hour - 6;
                if(hour <= 0){
                    hour = hour + 24;
                    day = day - 1;
                }
                    day = day + ' ';
                    hour = hour + ':';
                    var minute = date.getMinutes();

                return year + month + day + hour + minute;
            }
        },

        mounted() {
            var username = localStorage.getItem('username')
            if(username != null && username.length > 0) {
                //donothing
            }else {
                this.$message('请先登陆');
                this.$router.push('login');
            }
            this.placeTheData();
        }
    }
</script>

<style scoped>
.handle-box{
    margin-bottom: 20px;
}
.handle-select{
    width: 120px;
}
.handle-input{
    width: 300px;
    display: inline-block;
}
</style>
