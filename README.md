# SSLI

- Simple Syntax Language Implementation

## Spec

- ㅇㅇ

## IntelliJ IDEA 프로젝트 설정

* IntelliJ IDEA로 'git clone [...]'을 통해 받은 폴더를 연다.
* [File] - [Settings] - [Plugins] - [Install plugin from disk...], ANTLR v4 Grammar Plugin을 설치한다.
* 'SimpleLexer.g4', 'SimpleParser.g4'를 오른쪽 클릭하고, Generate ANTLR Recognizer를 누른다. ('gen' 폴더 생성됨)
* [File] - [Project Structure] - [Project], Project SDK를 1.8버전으로 설정한다.
* 같은 탭에서, Project Language Level을 (8 - Lambdas, ...)로 설정한다.
* 같은 탭에서, Project Compiler Output을 원하는 곳으로 설정한다. ('[프로젝트 경로]/out' 추천)
* [File] - [Project Structure] - [Modules] - [Sources], 'src' 폴더와 'gen' 폴더를 Sources로 마크한다.
* 같은 탭의 오른쪽에서 'gen'의 속성(P모양)을 누르고 'For generated sources'에 체크한다.
* [File] - [Project Structure] - [Libraries], '[프로젝트 경로]/lib/antlr-4.x-complete.jar'를 추가한다.
* 빌드를 해보자. Edit Configuration을 통해 다른 빌드 옵션을 만들 수도 있다.
* (추가 빌드 옵션) Main class: org.antlr.v4.gui.TestRig
* (추가 빌드 옵션) Program arguments : Simple prgm -gui test/TestLanguage.ssli
* (추가 빌드 옵션) Working directory: [프로젝트 경로]
* (추가 빌드 옵션) 위와 같이 하면 Test Case에 대한 Parse Tree를 GUI로 볼 수 있다.

