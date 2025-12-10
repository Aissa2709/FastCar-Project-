CONFIGURATION DU DRIVER MySQL POUR FASTCAR LOCATION
====================================================

Le projet FastCar nécessite le driver MySQL JDBC pour fonctionner avec la base de données.

ÉTAPES À SUIVRE :

1. TÉLÉCHARGER LE DRIVER MySQL CONNECTOR/J
   ==========================================
   - Allez sur : https://dev.mysql.com/downloads/connector/j/
   - Téléchargez la version 8.0+ (compatible avec MySQL 8.0+)
   - Choisissez le format : "mysql-connector-java-X.X.X.jar"

2. AJOUTER LE JAR AU PROJET
   ==========================
   - Placez le fichier mysql-connector-java-X.X.X.jar dans ce dossier (lib/)
   
3. CONFIGURER VOTRE IDE (VS Code avec Java Extension)
   ====================================================
   
   Option A : Utiliser .classpath (si vous avez Eclipse/Java Project)
   - Assurez-vous que .classpath inclut lib/*.jar
   - Exemple :
     <classpathentry kind="lib" path="lib/mysql-connector-java-8.0.XX.jar"/>
   
   Option B : Compiler et exécuter avec javac/java
   - Compilez : javac -cp lib/mysql-connector-java-8.0.XX.jar -d bin src/**/*.java
   - Exécutez : java -cp lib/mysql-connector-java-8.0.XX.jar:bin com.fastcar.ui.MainFrame
   
   Option C : Configurer la compilation dans VS Code
   - Modifiez .vscode/settings.json ou créez tasks.json pour inclure lib/

4. VÉRIFIER LA CONFIGURATION
   ==========================
   - Vérifiez que XAMPP MySQL est en cours d'exécution
   - Vérifiez que la base fastcar_location a été créée
   - Les identifiants par défaut : root / (pas de mot de passe)

ALTERNATIVE - TÉLÉCHARGEMENT DIRECT
====================================
URL directe (version 8.0.33) :
https://dev.mysql.com/downloads/file/?id=517499

Après téléchargement, décompressez et placez le JAR dans ce dossier.

PROBLÈMES COURANTS
==================
- "ClassNotFoundException: com.mysql.cj.jdbc.Driver"
  → Le JAR n'est pas dans le classpath
  
- "SQLException: Connection refused"
  → MySQL n'est pas en cours d'exécution ou les identifiants sont incorrects
  
- "Unknown database 'fastcar_location'"
  → Le script de création de la base n'a pas été exécuté
