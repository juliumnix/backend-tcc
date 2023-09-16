package com.udesc.reactflutternativeAndroid.utils

object GithubActionGenerator {
    fun generate() : String{
        return "name: Build and Verify\n" +
                "\n" +
                "on:\n" +
                "  push:\n" +
                "    branches:\n" +
                "      - main\n" +
                "\n" +
                "jobs:\n" +
                "  build:\n" +
                "    runs-on: ubuntu-latest\n" +
                "\n" +
                "    steps:\n" +
                "    - name: Checkout code\n" +
                "      uses: actions/checkout@v2\n" +
                "\n" +
                "    - name: Install Node.js\n" +
                "      uses: actions/setup-node@v2\n" +
                "      with:\n" +
                "        node-version: '18.17.0' # Altere para a versão desejada do Node.js\n" +
                "\n" +
                "    - name: Install React Native dependencies\n" +
                "      run: |\n" +
                "        npm install\n" +
                "        # Outras ações que você desejar realizar para o React Native\n" +
                "\n" +
                "    - name: Install Flutter\n" +
                "      run: |\n" +
                "        sudo snap install flutter --classic\n" +
                "        flutter --version\n" +
                "        # Outras ações que você desejar realizar para o Flutter\n" +
                "\n" +
                "    - name: Install Flutter dependencies\n" +
                "      run: |\n" +
                "        cd flutter_module\n" +
                "        flutter pub get\n"
    }
}