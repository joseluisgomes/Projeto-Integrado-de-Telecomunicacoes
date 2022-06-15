
# Sistema de Monitorização de Estações Meteorológicas 🌡

Sistema sensor terminal para aquisição de dados de sensores (humidade, temperatura e pressão atmosférica) e envio via _wireless_ para um dispositivo central.

## Authors

* [@joseluisgomes](https://github.com/joseluisgomes)
* [@alexthenotsogreat](https://github.com/alexthenotsogreat)
* [@Catarinaneves23](https://github.com/Catarinaneves23)
* [@luisandre-oliveira](https://github.com/luisandre-oliveira)

## Etapas do projeto

* Desenvolvimento de um dispositivo central (Gateway).
* Planeamento e implementação de base de dados com interface através dum servidor na rede.
* Programação de aplicações em redes Internet seguindo o paradigma de cliente/servidor.

## Enquadramento do projeto

A figura 1 ilustra uma visão geral da arquitetura do sistema completo. Tal como a figura sugere, o sistema desenvolvido  permite:

* Aquisição de dados de sistemas sensores (estações meteorológicas).
* Transferência dessa informação através de redes sem fios **BLE** (_Bluetooth Low Energy_).
* Armazenamento e acesso da informação referida através de uma base de dados relacional e um cliente/servidor _web_.

![diagrama_pit](https://user-images.githubusercontent.com/70901488/173863967-203a8bbe-5296-44a0-b021-3a1d19ac379b.png)
<div align = "center">Figura 1 - Diagrama do sistema terminal.</div>

## _Hardware_

|     Componentes     |    Descrição     |
| :-----------------: | :--------------: |
| `ESP32-DevKitC-32D` | 2 placas: uma para a implementação do sistema sensor e outra para o gateway BLE/Wi-Fi         |
| `DHT11`             | Sensor de temperatura e humidade                 |
| `BME280`            | Sensor de pressão barométrica                 |
| Breadboard        | Interface de conexão entre os circuitos                 |
| Fios de ligação     | Fios responsáveis pela conexão dos componentes necessários                |
| Computadores        | 2 PCs: um para a ligação ao Sistema Sensor e o restante para a ligação ao Gateway|

## _Software_

<p align="center">
  <a href="https://skillicons.dev">
    <img src="https://skillicons.dev/icons?i=arduino,vscode,idea,spring,mysql"/>
  </a>
</p>
