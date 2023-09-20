package com.udesc.reactflutternativeAndroid.utils

object ReadmeGenerator {
    private var reactTable: String = ""
    private var flutterTable: String = ""

    fun setReactTable(table: String) {
        reactTable = table;
    }

    fun generateReactTable(): String {
        return reactTable;
    }

    fun setFlutterTable(table: String) {
        flutterTable = table;
    }

    fun generateFlutterTable(): String {
        return flutterTable;
    }


    fun generate(): String {
        return "# Instruções para rodar seu projeto\n" +
                "\n" +
                "Para buildar seu app, será necessário alguns pré-requisitos:\n" +
                "\n" +
                "## Node\n" +
                "\n" +
                "Ter em sua máquina o node **v18.17.0**\n" +
                "\n" +
                "## Flutter\n" +
                "\n" +
                "Ter em sua máquina o Flutter **3.10.6**\n" +
                "\n" +
                "### Atenção ⚠\uFE0F\n" +
                "\n" +
                "É de responsabilidade do usuário a instalação das ferramentas antes citadas\n" +
                "\n" +
                "\n" +
                "\n" +
                "## Instalação\n" +
                "\n" +
                "Instale o projeto com npm na raiz do projeto.\n" +
                "\n" +
                "```\n" +
                "  npm install \n" +
                "```\n" +
                "\n" +
                "Caso necessário, também instale as dependencias do flutter dentro da pasta flutter_module.\n" +
                "\n" +
                "```\n" +
                "  flutter pub get \n" +
                "```\n" +
                "Com os dados instalados, poderá abrir seu projeto no Android Studio, e buildá-lo gerando seu apk com ambos as plataformas.\n" +
                "\n" +
                "## Como desenvolver\n" +
                "\n" +
                "Para rodar o projeto React Native, na pasta raiz, rode o seguinte comando.\n" +
                "\n" +
                "```\n" +
                "  npm start \n" +
                "```\n" +
                "\n" +
                "Para rodar o projeto Flutter, na pasta flutter_module, rode o seguinte comando.\n" +
                "\n" +
                "```\n" +
                "  flutter attach \n" +
                "```\n" + reactTable + flutterTable
    }
}