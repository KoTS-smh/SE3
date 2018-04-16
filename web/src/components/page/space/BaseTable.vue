<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-menu"></i> 表格</el-breadcrumb-item>
                <el-breadcrumb-item>任务列表</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="handle-box">
            <el-button type="primary" icon="delete" class="handle-del mr10" @click="delAll">批量删除</el-button>
            <el-select v-model="select_cate" placeholder="筛选任务" class="handle-select mr10">
                <el-option key="1" label="已完成" value="已完成"></el-option>
                <el-option key="2" label="未完成" value="未完成"></el-option>
                <el-option key="3" label="草稿" value="草稿"></el-option>
            </el-select>
            <el-input v-model="select_word" placeholder="筛选关键词" class="handle-input mr10"></el-input>
            <el-button type="primary" icon="search" @click="search">搜索</el-button>
            <el-button type="primary"  @click="newTask">新建</el-button>
        </div>
        <el-table :data="data" border style="width: 100%" ref="multipleTable" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column prop="date" label="日期" sortable width="150">
            </el-table-column>
            <el-table-column prop="name" label="任务名称" width="120">
            </el-table-column>
            <el-table-column prop="state" label="状态" width="120">
            </el-table-column>
            <el-table-column prop="address" label="备注" :formatter="formatter">
            </el-table-column>
            <el-table-column label="操作" width="180">
                <template slot-scope="scope">
                    <el-button size="small"
                            @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                    <el-button size="small" type="danger"
                            @click="handleDelete(scope.$index, scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <div class="pagination">
            <el-pagination
                    @current-change ="handleCurrentChange"
                    layout="prev, pager, next"
                    :total="1000">
            </el-pagination>
        </div>

        <el-dialog title="修改任务信息" :visible.sync="dialogVisible">
            <el-form :model="selectTable">
                <el-form-item label="任务名称" label-width="100px">
                    <el-input v-model="selectTable.name" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="日期" label-width="100px">
                    <el-input v-model="selectTable.date" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="状态" label-width="100px">
                    <el-input v-model="selectTable.state" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="备注" label-width="100px">
                    <el-input v-model="selectTable.address" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item>

                <div class="content-title">支持拖拽</div>
                <el-upload
                    class="upload-demo"
                    drag
                    action="/api/posts/"
                    multiple>
                    <i class="el-icon-upload"></i>
                    <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                    <div class="el-upload__tip" slot="tip">只能上传jpg/png文件，且不超过500kb</div>
                </el-upload>

                <img class="pre-img" :src="src" alt="">
                <!-- <vue-core-image-upload :class="['pure-button','pure-button-primary','js-btn-crop']"
                                    :crop="true"
                                    text="上传图片"
                                    url="/api/posts/"
                                    extensions="png,gif,jpeg,jpg"
                                    @:imageuploaded="imageuploaded"
                                    @:errorhandle="handleError"></vue-core-image-upload> -->

                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
            </div>
        </el-dialog>


        <el-dialog title="新建任务" :visible.sync="newDialogVisible">
            <el-form>
                <el-form-item label="任务名称" label-width="100px">
                    <el-input  auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="日期" label-width="100px">
                    <el-input  auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="状态" label-width="100px">
                    <el-input  auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="备注" label-width="100px">
                    <el-input  auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item>
                <br>

                <el-upload
                    class="upload-demo"
                    drag
                    action="/api/posts/"
                    multiple
                    style="text-align:center">
                    <i class="el-icon-upload"></i>
                    <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                    <div class="el-upload__tip" slot="tip">只能上传jpg/png文件，且不超过500kb</div>
                </el-upload>

                <img class="pre-img" :src="src" alt="">
                <!-- <vue-core-image-upload :class="['pure-button','pure-button-primary','js-btn-crop']"
                                    :crop="true"
                                    text="上传图片"
                                    url="/api/posts/"
                                    extensions="png,gif,jpeg,jpg"
                                    @:imageuploaded="imageuploaded"
                                    @:errorhandle="handleError"></vue-core-image-upload> -->

                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="newDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="newDialogVisible = false">确 定</el-button>
            </div>
        </el-dialog>
    </div>


</template>

<script>
    import VueCoreImageUpload  from 'vue-core-image-upload';
    export default {
        data() {
            return {
                url: './static/testtable.json',
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
        created(){
            this.getData();
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
            getData(){
                let self = this;
                // if(process.env.NODE_ENV === 'development'){
                //     self.url = '/ms/table/list';

                // };
                self.$axios.get(self.url, {page:self.cur_page}).then((res) => {
                    self.tableData = res.data.list;
                })
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
                //this.$message('编辑第'+(index+1)+'行');
                this.selectTable = row;
                this.address = row.address;
                this.dialogVisible = true;

            },
            handleDelete(index, row) {
                this.$message.error('删除第'+(index+1)+'行');
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
                this.newDialogVisible = true;
            }
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
