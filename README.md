# School telegram bot by Kazakpayev Solomon and Bogdan Pronin

Данный бот имеет интерфейс как для простого ученика, так для учителя и администратора.

#### Интерфейс ученика
> `/help` - запрос списка команд бота.
>
> `/homework` - запрос домашнего задания на введенную дату.
>
> `/controls` - запрос контрольных и самостоятельных работ на текущую, следующую неделю и за все время.
>
> `/schedule` - просмотр расписания по введенному дню недели.

#### Интерфейс учителя
> `/posthometask` - добавление в базу домашнего задания на дату.
>
> `/postattendance` - добавление в базу графика посещаемости учеников.
>
> `/postattendancehelp` - запрос примера Excel файла для заполнения графика посещаемости учеников.
>
> `/postcontrol` - добавление в базу графика проверочных работ с помощью Excel файла.
>
> `/postcontrolhelp` - запрос примера Excel файла для заполнения графика проверочных работ.
>
> `/attendance` - запрос графика посещаемости учеников на указанную дату для указанного класса.

#### Интерфейс администратора
> `/postschedule` - добавление в базу расписания с помощью Excel файла.
>
> `/postschedulehelp` - запрос примера Excel файла для заполнения расписания.


Также этот бот *автоматически оповещает* Вас о контрольных работах за день до их проведения. Каждый вечер бот, заботясь о Вас, уведомляет о том, что необходимо собрать рюкзак, и для удобства отправляет расписание на следующий день.

