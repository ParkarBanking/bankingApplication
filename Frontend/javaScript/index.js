function login_cust() {
  var email = document.getElementById("email").value;
  var password = document.getElementById("password").value;

  var login_data = {
    emailId: email,
    password: password,
  };

  $.ajax({
    type: "POST",
    url: "http://bankingapplication-env.eba-c5izrjwd.ap-south-1.elasticbeanstalk.com/customer/auth/loginCustomer",
    contentType: "application/json",
    data: JSON.stringify(login_data),
    dataType: "json",

    success: function (data) {
      // console.log(JSON.stringify(data.status));
      // alert(JSON.stringify(data.status));
      window.location.href = "./pages/home.html";
    },

    error: function (error) {
      Swal.fire({
        icon: "error",
        title: "Oops...",
        text: error.responseJSON.message,
      });
      console.log(error);
      // alert(`error: ${error.responseJSON.message}`);
    },
  });
}

function register_cust() {
  var firstName = document.getElementById("firstName").value;
  var middleName = document.getElementById("middleName").value;
  var lastName = document.getElementById("lastName").value;
  var emailId = document.getElementById("email").value;
  var password = document.getElementById("password").value;
  var aadharNumber = document.getElementById("aadharNumber").value;
  var panNumber = document.getElementById("panNumber").value;
  var mobileNumber = document.getElementById("mobileNumber").value;
  var dateOfBirth = document.getElementById("dateOfBirth").value;
  var address = document.getElementById("address").value;

  var register_data = {
    firstName: firstName,
    middleName: middleName,
    lastName: lastName,
    aadharNumber: aadharNumber,
    panNumber: panNumber,
    dateOfBirth: dateOfBirth,
    address: address,
    mobileNumber: mobileNumber,
    emailId: emailId,
    password: password,
  };

  $.ajax({
    type: "POST",
    url: "http://bankingapplication-env.eba-c5izrjwd.ap-south-1.elasticbeanstalk.com/customer/auth/registerCustomer",
    contentType: "application/json",
    data: JSON.stringify(register_data),
    dataType: "json",

    success: function () {
      window.location.href = "http://127.0.0.1:5500/index.html";
    },

    error: function (error) {
      console.log(error);
      alert(`error: ${error.value}`);
    },
  });
}

function update_profile() {
  var customerId = document.getElementById("customerId").value;
  var firstName = document.getElementById("firstName").value;
  var middleName = document.getElementById("middleName").value;
  var lastName = document.getElementById("lastName").value;
  var address = document.getElementById("address").value;

  var profileUpdate_data = {
    customerId: customerId,
    firstName: firstName,
    middleName: middleName,
    lastName: lastName,
    address: address,
  };

  $.ajax({
    type: "POST",
    url: "http://localhost:8080/customer/auth/updateCustomer",
    contentType: "application/json",
    data: JSON.stringify(profileUpdate_data),
    dataType: "json",

    success: function () {
      window.location.href = "http://127.0.0.1:5500/pages/home.html";
    },

    error: function (error) {
      alert(`error: ${error.value}`);
    },
  });
}

function update_password() {
  var emailId = document.getElementById("email").value;
  var newPassword = document.getElementById("newPassword").value;
  var confirmPassword = document.getElementById("confirmPassword").value;

  var passwordUpdate_data = {
    emailId: emailId,
    newPassword: newPassword,
    confirmPassword: confirmPassword,
  };

  $.ajax({
    type: "POST",
    url: "http://localhost:8080/customer/auth/updatePassword",
    contentType: "application/json",
    data: JSON.stringify(passwordUpdate_data),
    dataType: "json",

    success: function () {
      window.location.href = "http://127.0.0.1:5500/pages/home.html";
    },

    error: function (error) {
      alert(`error: ${error.value}`);
    },
  });
}

function add_money() {
  var accountNumber = document.getElementById("accountNumber").value;
  var amount = document.getElementById("amount").value;

  var amount_data = {
    accountNumber: accountNumber,
    accountBalance: amount,
  };

  $.ajax({
    type: "POST",
    url: "http://localhost:8080/customer/transaction/addBalance",
    contentType: "application/json",
    data: JSON.stringify(amount_data),
    dataType: "json",

    success: function () {
      window.location.href = "http://127.0.0.1:5500/pages/home.html";
    },

    error: function (error) {
      alert(`error: ${error.responseJSON.message}`);
    },
  });
}

function transfer_money() {
  var senderAccount = document.getElementById("senderAccountNumber").value;
  var receiverAccount = document.getElementById("receiverAccountNumber").value;
  var amount = document.getElementById("amount").value;
  var branchName = document.getElementById("branchName").value;
  var ifsc = document.getElementById("ifsc").value;
  var transactionPin = document.getElementById("transactionPin").value;

  var transfer_data = {
    senderAccount: senderAccount,
    receiverAccount: receiverAccount,
    amount: amount,
    branchName: branchName,
    ifsc: ifsc,
    transactionPin: transactionPin,
  };

  $.ajax({
    type: "POST",
    url: "http://bankingapplication-env.eba-c5izrjwd.ap-south-1.elasticbeanstalk.com/customer/transaction/transferMoney",
    contentType: "application/json",
    data: JSON.stringify(transfer_data),
    dataType: "json",

    success: function () {
      window.location.href = "http://127.0.0.1:5500/pages/home.html";
    },

    error: function (error) {
      console.log(error);
      alert(`error: ${error.value}`);
    },
  });
}
