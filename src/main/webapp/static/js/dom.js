import {dataHandler} from "./data_handler.js";

export let dom = {
    init: function () {
        dom.addEventToCartButtons();
        dom.addEventToQuantityInput();
        dom.addEventToItems();
        dom.addEventToCheckoutCheckbox();
        dom.changeVisibility();

    },
    addEventToCheckoutCheckbox: function(){
    let a = document.querySelector('.checkout-checkbox');
    if (a!=null) {
        document.querySelector('.checkout-checkbox').addEventListener('change', dom.changeVisibility);

    }
    },
    addEventToCartButtons: function () {
        let add_cart_btn = document.querySelectorAll(".add-to-cart");
        for(let btn of add_cart_btn) {
            btn.addEventListener('click', dom.addToCart);
        }
    },
    addEventToQuantityInput: function() {
        let inputs = document.querySelectorAll('.quantity-num');
        for(let input of inputs) {
            input.addEventListener('click', dom.handleQuantityValue);
            input.addEventListener('keyup', dom.handleQuantityValue);
        }
    },
    addEventToItems: function() {
        let items = document.querySelectorAll('.item');
        for(let item of items) {
            item.addEventListener('click', dom.editCart);
        }
    },
    addToCart: function (event) {
        let prodId = event.target.dataset.itemId;
        dataHandler.addToCart(prodId, function (response) {
            document.querySelector(".shopping_cart_icon span").dataset.count = response["cartSize"];
        })
    },
    changeVisibility: function () {

        let shippingDiv = document.querySelector('.checkout-shipping-address');
        let cb = document.querySelector('.checkout-checkbox');

        if (cb.checked===true) {
            dom.changeInputReguired(false);
            shippingDiv.style.display = 'none';
        } else {
            shippingDiv.style.display = 'block';
            dom.changeInputReguired(true);
        }
    },
    changeInputReguired: function(mode){
        let shipping_inputs = document.querySelectorAll(".shipping");
        for (let shipping_input of shipping_inputs)
            shipping_input.required = mode;
    },
    editCart: function (e) {
        let item = e.currentTarget;
        let classList = e.target.classList;
        if(classList.contains("refresh") || classList.contains("remove")) {
            let input = e.currentTarget.querySelector(".quantity-num");
            let prodId = e.currentTarget.dataset.prodId;
            let newProdsNumber = classList.contains("remove") ? 0 : input.value;

            dataHandler.editCart(prodId, newProdsNumber.toString(), function (response) {
                let newProdPrice = response["prodTotalPrice"];
                let totalPrice = response["totalPrice"];
                if(classList.contains("remove")) {
                    if(item.parentNode.children.length === 1) window.location.reload();
                    item.remove();
                }
                item.querySelector(".subtotal-num").textContent = `$${newProdPrice.toFixed(1)}`;
                document.querySelector(".total").textContent = `Total $${totalPrice.toFixed(1)}`;
                input.dataset.currentNum = input.value;
            })
        }
    },
    handleQuantityValue: function (e) {
        let value = e.target.value;
        if(parseInt(value) < 1 || e.key === "e") {
            e.target.value = 1;
        }
    },
};