<template>
  <div>
    <input id="login" name="login" placeholder="Login" @input="updateTheLogin($event.target.value)">
    <input type="password" id="password" name="password" placeholder="Password" @input="updateThePassword($event.target.value)">
    <input id="code" name="code" placeholder="Code" @input="updateTheCode($event.target.value)">
    <br>
    <Button label=Login icon="pi pi-times"
            @click="loginFunction" class="button button-white"/>
    <Button label=Register icon="pi pi-times"
            @click="registerFunction" class="button button-white"/>
    <Button label=Logout icon="pi pi-times"
            @click="logoutFunction" class="button button-white"/>
    <br>
    <br>
    <div>
      <textarea @input="updateSecretKey($event.target.value)" name="Secret Key" placeholder="Secret Key"></textarea>
      <br>
      <textarea @input="updateText($event.target.value)" name="New Note" placeholder="New Note"></textarea>
      <br>
      <Button label='Create new note' icon="pi pi-times"
              @click="createNote" class="button button-white"/>
      <Button label='Get notes' icon="pi pi-times"
              @click="getNotes" class="button button-white"/>
      <br>
      <br>
      <tbody>
      <tr v-for="item in notes">
        <td v-html="item"></td>
      </tr>
      </tbody>
    </div>
  </div>
</template>
<script>
import axios from 'axios';
import Button from "primevue/button";
import CryptoJS from 'crypto-js';
import DOMPurify from 'dompurify';

export default {

  name: "loginPage",

  components: {Button},

  beforeMount() {
    this.secretKey = "";
  },

  data() {
    return {
      notes: [],
      encrypteds: []
    };
  },

  methods: {

    updateText(value) {
      this.text = value;
    },

    updateTheLogin(value){
      this.email = value
    },

    updateThePassword(value){
      this.password = value
    },

    updateSecretKey(value) {
      this.secretKey = value;
    },

    updateTheCode(value) {
      this.code = value;
    },

    logoutFunction() {
      localStorage.clear();
      document.location.reload();
    },

    async loginFunction() {
      let unblockCode = this.code;
      if (unblockCode === undefined) unblockCode = "";
      let postData = {
        password : this.password,
        email : this.email,
        code : unblockCode,
      };
      let axiosConfig = {
        headers: {
          'Content-Type': 'application/json;charset=UTF-8',
          "Access-Control-Allow-Origin": "*",
        }
      };
      axios.post("https://localhost:8443/api/login", postData, axiosConfig)
          .then((res) => {
            localStorage.setItem("token", res.headers.accesstoken);
            alert("Success login");
          })
          .catch((err) => {
          })
    },

    registerFunction() {
      let postData = {
        password : this.password,
        email : this.email,
      };
      let axiosConfig = {
        headers: {
          'Content-Type': 'application/json;charset=UTF-8',
          "Access-Control-Allow-Origin": "*",
        }
      };
      axios.post("https://localhost:8443/api/registration", postData, axiosConfig)
          .then((res) => {
            alert("now you can login");
          })
          .catch((err) => {
          })
    },

    createNote() {
      if (this.secretKey.length < 8) {
        alert("secret key must have at least 8 characters");
        return;
      }
      //const encrypt = CryptoJS.AES.encrypt(this.text, this.secretKey).toString();
      const ct = CryptoJS.AES.encrypt(this.text, this.secretKey);
      //var saltHex = ct.salt.toString();
      //var ctHex = ct.ciphertext.toString();
      //var ivHex = ct.iv.toString();
      //console.log("salt", saltHex);
      //console.log("iv", ivHex);
      const encrypt=ct.toString();
      let postData = {
        note: encrypt,
        token: localStorage.getItem("token")
      };
      let axiosConfig = {
        headers: {
          'Content-Type': 'application/json;charset=UTF-8',
          "Access-Control-Allow-Origin": "*",
        }
      };
      axios.post("https://localhost:8443/api/note/new", postData, axiosConfig)
          .then((res) => {
            alert("you create new note!");
          })
          .catch((err) => {
          })
    },

    getNotes() {
      let postData = {
        token: localStorage.getItem("token")
      };
      let axiosConfig = {
        headers: {
          'Content-Type': 'application/json;charset=UTF-8',
          "Access-Control-Allow-Origin": "*",
        }
      };
      axios.post("https://localhost:8443/api/note/get", postData, axiosConfig)
          .then((res) => {
            this.encrypteds = res.data;
            this.decodeNotes();
          })
          .catch((err) => {
          })
    },

    async decodeNotes() {
      this.notes = [];
      let i = 0;

      while (i < this.encrypteds.length) {
        this.notes[i] = CryptoJS.AES.decrypt(this.encrypteds[i].text, this.secretKey).toString(CryptoJS.enc.Utf8);
        this.notes[i] = DOMPurify.sanitize(this.notes[i], {
          ALLOWED_TAGS: ['b', 'i', 'h1', 'h2', 'h3', 'h4', 'h5', 'img', 'a'],
          ALLOWED_ATTR: []
        });
        i++;
      }
      console.log(this.notes);
    }
  }
}
</script>

<style scoped>
header {
  line-height: 1.5;
  max-height: 100vh;
}

.logo {
  display: block;
  margin: 0 auto 2rem;
}

nav {
  width: 100%;
  font-size: 12px;
  text-align: center;
  margin-top: 2rem;
}

nav a.router-link-exact-active {
  color: var(--color-text);
}

nav a.router-link-exact-active:hover {
  background-color: transparent;
}

nav a {
  display: inline-block;
  padding: 0 1rem;
  border-left: 1px solid var(--color-border);
}

nav a:first-of-type {
  border: 0;
}

@media (min-width: 1024px) {
  header {
    display: flex;
    place-items: center;
    padding-right: calc(var(--section-gap) / 2);
  }

  .logo {
    margin: 0 2rem 0 0;
  }

  header .wrapper {
    display: flex;
    place-items: flex-start;
    flex-wrap: wrap;
  }

  nav {
    text-align: left;
    margin-left: -1rem;
    font-size: 1rem;

    padding: 1rem 0;
    margin-top: 1rem;
  }
}
</style>
