import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import axios from 'axios'
import VueMarkdownEditor from '@kangc/v-md-editor';
import '@kangc/v-md-editor/lib/style/base-editor.css';
import vuepressTheme from '@kangc/v-md-editor/lib/theme/vuepress.js';
import VMdPreview from '@kangc/v-md-editor/lib/preview';
import '@kangc/v-md-editor/lib/style/preview.css';
import githubTheme from '@kangc/v-md-editor/lib/theme/github.js';
import '@kangc/v-md-editor/lib/theme/style/github.css';
import hljs from 'highlight.js';

Vue.config.productionTip = false;
Vue.use(ElementUI);
Vue.prototype.$axios = axios;
VueMarkdownEditor.use(vuepressTheme);
Vue.use(VueMarkdownEditor);
VMdPreview.use(githubTheme, {
  Hljs: hljs,
});
Vue.use(VMdPreview);

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
