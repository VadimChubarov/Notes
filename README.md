# Notes
Android приложение для сохранения записей, списков (чеклистов) и жд билетов

<p> 
  <img src="https://github.com/VadimChubarov/Screenshots-repo/blob/master/Screenshot_20181114-134534.png" width="200"/>
  <img src="https://github.com/VadimChubarov/Screenshots-repo/blob/master/Screenshot_20181114-134538.png" width="200"/>
  <img src="https://github.com/VadimChubarov/Screenshots-repo/blob/master/Screenshot_20181114-134747.png" width="200"/>
  <img src="https://github.com/VadimChubarov/Screenshots-repo/blob/master/Screenshot_20181114-135427.png" width="200"/>
<p/>

Основные функции :

- создание и хранение обычных записей (заметок) 

  <img src="https://github.com/VadimChubarov/Screenshots-repo/blob/master/Screenshot_20181114-134902.png" width="200"/>
  <img src="https://github.com/VadimChubarov/Screenshots-repo/blob/master/Screenshot_20181114-134910.png" width="200"/>

- создание контрольных списков c чекбоксами (довольно удобно при сборе вещей в поездку или большой закупке в магазине)

  <img src="https://github.com/VadimChubarov/Screenshots-repo/blob/master/Screenshot_20181114-134936.png" width="200"/>
  <img src="https://github.com/VadimChubarov/Screenshots-repo/blob/master/Screenshot_20181114-135028.png" width="200"/>
  <img src="https://github.com/VadimChubarov/Screenshots-repo/blob/master/Screenshot_20181114-135036.png" width="200"/>

- извлечение основной информации из жд билетов и хранение ее в виде специальной заметки со ссылкой на сам билет. 

  После скачивания жд билета с электронной почты, нужно просто выбрать его в приложении (папка Downloads откроется автоматически) 
  произойдет обработка билета (PDF формата) и необходимая информация будет сохранена в удобной форме, также будет сохранена ссылка 
  на билет, чтобы его показать при проверке из приложения (нет необходимости больше искать его в Downloads).
  
  Функция помогает упорядочить жд билеты (удобно если едет компания) и избавляет от необходимости выискивать нужную информацию 
  (ФИО, вагон, место , дата, время) в билете среди прочей, также искать его постоянно в файловой системе, чтобы посмотреть или показать.
  
  <img src="https://github.com/VadimChubarov/Screenshots-repo/blob/master/Screenshot_20181114-135115.png" width="200"/>
  <img src="https://github.com/VadimChubarov/Screenshots-repo/blob/master/Screenshot_20181114-135135.png" width="200"/>
  <img src="https://github.com/VadimChubarov/Screenshots-repo/blob/master/Screenshot_20181114-135143.png" width="200"/>
  <img src="https://github.com/VadimChubarov/Screenshots-repo/blob/master/Screenshot_20181114-135149.png" width="200"/>
  <img src="https://github.com/VadimChubarov/Screenshots-repo/blob/master/Screenshot_20181114-134815.png" width="200"/>
  
  Навигация и сортировка элементов выполнена в виде вкладок (TabBar), также есть поиск (ищет как по названию, так и по содержимому)
  
  <img src="https://github.com/VadimChubarov/Screenshots-repo/blob/master/Screenshot_20181114-134802.png" width="200"/>
  <img src="https://github.com/VadimChubarov/Screenshots-repo/blob/master/Screenshot_20181114-134806.png" width="200"/>
  <img src="https://github.com/VadimChubarov/Screenshots-repo/blob/master/Screenshot_20181114-134815.png" width="200"/>
  <img src="https://github.com/VadimChubarov/Screenshots-repo/blob/master/Screenshot_20181114-135349.png" width="200"/>
  
  Технологии:
  
  - Для парсинга PDF была использована библиотека iText7
  - Хранение данных SQLite
  
  Это мое первое приложение на Android, поэтому архитектура довольно спутанная, но приложение работоспособно.
 
  

  

