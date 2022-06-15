
# Sistema de Monitorização de Estações Meteorológicas 

Sistema sensor terminal para aquisição de dados de sensores (humidade, temperatura e pressão atmosférica) e envio via _wireless_ para um dispositivo central.

Etapas do projeto 
-
* Desenvolvimento de um dispositivo central (Gateway). 
* Planeamento e implementação de base de dados com interface através dum servidor na rede.
* Programação de aplicações em redes Internet seguindo o paradigma de cliente/servidor.

Enquadramento do projeto
-
A figura 1 ilustra uma visão geral da arquitetura do sistema completo. Tal como a figura sugere, o sistema desenvolvido  permite:
* Aquisição de dados de sistemas sensores (estações meteorológicas).
* Transferência dessa informação através de redes sem fios **BLE** (_Bluetooth Low Energy_).
* Armazenamento e acesso da informação referida através de uma base de dados relacional e um cliente/servidor _web_.

![diagrama_pit](https://user-images.githubusercontent.com/70901488/173863967-203a8bbe-5296-44a0-b021-3a1d19ac379b.png)
<div align = "center">Figura 1 - Diagrama do sistema terminal.</div>


## Authors

- [@joseluisgomes](https://github.com/joseluisgomes)
- [@alexthenotsogreat](https://github.com/alexthenotsogreat)
- [@Catarinaneves23](https://github.com/Catarinaneves23)
- [@luisandre-oliveira](https://github.com/luisandre-oliveira)


## Screenshots

![login](https://user-images.githubusercontent.com/70901488/173867927-14783e5d-86b5-42d7-8126-84984e8bc2b6.png)<div align = "center">Figura 2 - Página inicial de login.</div>

![site_pit](https://user-images.githubusercontent.com/70901488/173867648-173ce534-98e4-4c56-9fa7-398d89e2f158.png)<div align = "center">Figura 3 - Página inicial do servidor web.</div>
