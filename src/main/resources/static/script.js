const myButton = document.getElementById("myButton");
myButton.addEventListener("click",(async function(){
    const params = {
        method: "POST",
        headers: {
            incrementCounter: true
        }
    }
    const endpoint = "/hello";
    const response = fetch(endpoint,params)
    .then(token => {return token.text()});
    myButton.value = response.value;
    console.log(response.value);
}))
