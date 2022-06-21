// noinspection JSUnusedGlobalSymbols

function getQueryVariable(variable) {
    let query = window.location.search.substring(1);
    let vars = query.split("&");
    for (let i = 0; i < vars.length; i++) {
        let pair = vars[i].split("=");
        if (decodeURI(pair[0]) === variable) {
            return decodeURI(pair[1]);
        }
    }
    return null;
}

function byteFormat(size) {
    let KB = 1 << 10;
    let MB = 1 << 20;
    let GB = 1 << 30;
    let result;
    if (size >= GB) {
        result = (size / GB).toFixed(2) + " GB";
    } else if (size >= MB) {
        result = (size / MB).toFixed(2) + " MB";
    } else if (size >= KB) {
        result = (size / KB).toFixed(2) + " KB";
    } else {
        result = size + " B";
    }
    return result;
}

let userInfo = {
    uid: sessionStorage.getItem("uid"),
    username: sessionStorage.getItem("username"),
    sex: sessionStorage.getItem("sex"),
    information: sessionStorage.getItem("information")
}

if (userInfo.uid === null || userInfo.uid === '') {
    window.open("login.html", "_self");
}

let app = Vue.createApp({
    data() {
        let items = [];
        let path = getQueryVariable("path");
        if (path === null || path === "") path = "/";
        if (path !== "/") {
            let n = path.lastIndexOf("/")
            let lastPath = path.substring(0, n);
            if (lastPath === "") lastPath = "/"
            let item = {
                path: lastPath,
                href: `index.html?path=${lastPath}`,
                i_class: "fa-solid fa-arrow-turn-up",
                name: ". . ",
                size: "-",
                type: "上级目录"
            };
            items.push(item);
        }
        return {
            username: userInfo.username,
            path: path,
            items: items
        }
    },
    methods: {
        logout() {
            axios("/api/user/logout", {
                method: "POST"
            }).then(function (resp) {
                let body = resp.data;
                if (body.code === 0) {
                    window.open("login.html", "_self");
                }
            });
        },
        contextmenu(ev) {
            let name = ev.target.attributes["data-name"].value;
            let path = ev.target.attributes["data-path"].value;
            if (name === ". . ") return false;
            if (confirm(`是否删除"${name}?"`)) {
                axios("/api/file/delete", {
                    method: "POST",
                    data: {path: path}
                }).then(function (resp) {
                    let body = resp.data;
                    if (body.code === 0) {
                        let key = -1;
                        for (key in app.items) {
                            if (app.items[key].name === name) {
                                break;
                            }
                        }
                        if (key !== -1) {
                            app.items.splice(key, 1);
                        }
                    } else {
                        alert(`删除"${name}"失败`);
                    }
                });
            }
        }
    }
}).mount("#app");

axios(`/api/file/list?path=${app.path}`).then(function (resp) {
    let body = resp.data;
    if (body.code === 0) {
        let list = body.data;
        let listKeys = Object.keys(list).sort((a, b) => {
            if (list[a].directory && list[b].directory) {
                return list[a].name.localeCompare(list[b].name);
            } else if (!list[a].directory && list[b].directory) {
                return 1
            } else if (list[a].directory && !list[b].directory) {
                return -1;
            } else {
                return list[a].name.localeCompare(list[b].name);
            }
        });

        for (let key of listKeys) {
            let d = list[key];
            let name = d.name;
            let path = d.path;
            let size = d.size;
            let directory = d.directory;
            let contentType = d.contentType;

            let href, i_class, type;
            if (directory) {
                href = `index.html?path=${path}`;
                i_class = "fa-solid fa-folder";
                size = '-';
                type = "文件夹"
            } else {
                href = `/api/file/download/${name}?path=${path}`;
                i_class = "fa-solid fa-file";
                size = byteFormat(size);
                type = contentType.split("/")[1];
            }

            let item = {
                path: path,
                href: href,
                i_class: i_class,
                name: name,
                size: size,
                type: type
            };
            app.items.push(item);
        }
    } else {
        window.open("login.html", "_self");
    }
});

let updateInfoModal = new bootstrap.Modal(document.getElementById('updateInfoModal'));

let modelApp = Vue.createApp({
    data() {
        return {
            uid: userInfo.uid,
            password: "",
            username: userInfo.username,
            sex: userInfo.sex,
            information: userInfo.information
        }
    },
    methods: {
        submit() {
            let data = {
                username: this.username,
                sex: this.sex,
                information: this.information
            }

            if (this.password !== "") {
                data.password = this.password;
            }

            axios("/api/user/update", {
                method: "POST",
                data: JSON.stringify(data),
                headers: {"content-type": "application/json"}
            }).then(function (resp) {
                let body = resp.data;
                if (body.code === 0) {
                    alert("修改成功")
                    updateInfoModal.hide();
                    if (modelApp.username !== userInfo.username) {
                        userInfo.username = modelApp.username;
                        app.username = modelApp.username;
                        sessionStorage.setItem("username", modelApp.username);
                    }
                    if (modelApp.sex !== userInfo.sex) {
                        userInfo.sex = modelApp.sex;
                        sessionStorage.setItem("sex", modelApp.sex);
                    }
                    if (modelApp.information !== userInfo.information) {
                        userInfo.information = modelApp.information;
                        sessionStorage.setItem("information", modelApp.information);
                    }
                } else {
                    alert(body.msg)
                }
            });
        }
    }
}).mount("#updateInfoModal");

let uploadModalApp = Vue.createApp({
    data() {
        return {
            path: app.path
        }
    },
    methods: {
        submit() {
            let path = this.path;
            let fileDom = document.getElementById("file");
            if (fileDom.files.length === 0) return false;

            let file = fileDom.files[0];
            console.log(path);
            console.log(file);

            if (!path.endsWith("/")) path += "/";
            let originPath = path + file.name;
            console.log(originPath);

            let fileReader = new FileReader();
            fileReader.readAsArrayBuffer(file);
            fileReader.addEventListener("loadend", function (ev) {
                let data = this.result;
                console.log(data);
                axios("/api/file/upload", {
                    method: "POST",
                    data: data,
                    headers: {
                        "path": encodeURI(originPath),
                        "cover": "true",
                    }
                }).then(function (resp) {
                    let body = resp.data;
                    if (body.code === 0) {
                        location.reload();
                    } else {
                        alert(body.msg);
                    }
                });
            });
        }
    }
}).mount("#uploadModal");

let mkdirApp = Vue.createApp({
    data() {
        return {
            path: app.path
        }
    },
    methods: {
        submit() {
            let path = this.path;
            if (path.indexOf("..") !== -1) {
                alert("路径中不能包含..");
                return false;
            }
            axios("/api/file/mkdir", {
                method: "POST",
                data: JSON.stringify({path: path}),
                headers: {"content-type": "application/json"}
            }).then(function (resp) {
                let body = resp.data;
                if (body.code === 0) {
                    location.reload();
                } else {
                    alert(body.msg);
                }
            });
        }
    }
}).mount("#mkdirModal");