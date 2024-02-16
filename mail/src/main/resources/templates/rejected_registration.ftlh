<#ftl encoding="UTF-8">
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Thank you for registration your ${object_type}, ${name}!</title>
    <style>
        #body {
            display: flex;
            flex-direction: column;
            font-family: "Roboto", "serif";
            align-items: center;
            background-color: #607FF2;
            font-size: 20px;
            height: 100%;
        }

        #div {
            display: block;
            flex-direction: column;
            margin: 10%;
            font-family: "Segoe UI", serif;
            background-color: white;
            padding: 3%;
            border-radius: 20px;
            color: black;
        }

        #a {
            text-decoration: none;
        }
    </style>
</head>
<body>
<div id="body">
    <div id="div">
        <h1 style="text-align: center">Rejected ${object_type} registration
            request</h1>
        <p>Hello, ${name}!</p>
        <p>
            We regret to inform you that the registration of
            your ${object_type} named '${object_name}' has been rejected.
            Please contact us for further details.
        </p>
        <p>With best regards, <br> Oleg Ryazancev</p>
        <a id="a" href="https://github.com/OlegRyazancev/online-shop">Github</a>
    </div>
</div>
</body>
</html>