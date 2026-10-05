<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Profile</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.3.1/css/all.min.css">
</head>
<body>
    <div class="mobile">
        <div class="top">
            <div class="btn"><i class="fa-solid fa-arrow-left-long"></i></div>
            <div class="btn" style="color: green;"><i class="fa-solid fa-pen-to-square"></i></div>
        </div>
        <div class="center">
            <div class="avt"></div>
            <div class="name">Nguyễn Trung Nguyên</div>
            <div class="id">066205001323</div>
        </div>
    </div>

</body>
</html>

<style>
    body {
        margin:0;
        display: flex;
        font-family: Arial, sans-serif;
    }
    .mobile {
        width: 100%;
        height: 100vh;
        background-color: #F7FDFF;
        padding: 20px;
    }

    .top {
        display: flex;
        justify-content: space-between;
    }
    .btn {
        width: 35px;
        height: 35px;
        border: 1px solid #DEDEDE;
        border-radius: 20%;
        display: flex;
        justify-content: center;
        align-items: center;
    }

    .avt {
        background-image: url('https://i.pinimg.com/originals/35/bc/08/35bc08d104a2ee152d0e884c252c8c4f.jpg');
        background-size: cover;
        width: 250px;
        height: 250px;
        border-radius: 50%;
        margin: 40% auto 20px auto;
    }
    .name {
        font-size: 35px;
        font-weight: 600;
        text-align: center;
    }

    .id {
        font-size: 25px;
        color: #797878;
        text-align: center;
    }
</style>