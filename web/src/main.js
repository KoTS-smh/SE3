import Vue from 'vue';
import App from './App';
import router from './router';
import axios from 'axios';
import ElementUI from 'element-ui';
import lightbox from 'vlightbox';
import 'element-ui/lib/theme-chalk/index.css';
import VueResource from 'vue-resource'
import VueLazyload from 'vue-lazyload'

Vue.use(VueLazyload)
import "babel-polyfill";

import store from './store'

import {Message} from 'element-ui'
const message = Message
Vue.prototype.$message = message


Vue.use(lightbox);
Vue.use(ElementUI);
Vue.use(VueResource);
Vue.prototype.$axios = axios;
new Vue({
    store,
    router,
    render: h => h(App),
    el: '#app',
    template: '<App/>',
    components: { App }
}).$mount('#app');
