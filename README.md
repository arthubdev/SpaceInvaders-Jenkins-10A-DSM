# Space Invaders con Jenkins

Proyecto Java sencillo creado para la Práctica 10 de integración continua. El juego usa Java Swing y se compila como un archivo JAR ejecutable.

## Integrantes

- Hugo Arcos Zúñiga
- Guillermo
- Obed Guzmán
- Andrik

## Compilación local

Se requiere un JDK instalado y disponible en `PATH`.

```bat
build.bat
```

El resultado se genera en `build\SpaceInvaders.jar`.

```bat
java -jar build\SpaceInvaders.jar
```

## Configuración de Jenkins

Crear un job de tipo **Freestyle project** y configurar:

1. La URL de este repositorio en **Source Code Management > Git**.
2. Un paso de compilación **Execute Windows batch command** con `call build.bat`.
3. En **Post-build Actions**, archivar el artefacto `build/SpaceInvaders.jar`.

También se incluye un `Jenkinsfile` opcional para crear un Pipeline.
