import pandas as pd
import matplotlib.pyplot as plt
data={
    "month":["January", "February", "March", "April", "May", "June"],
    "sales":[1500, 2000, 2500, 2500, 2000, 4000],
    "profit":[500, 700, 800, 1000, 1200, 1500]
}
#line graph
df=pd.DataFrame(data)
plt.plot(df["month"], df["sales"])
plt.title("Monthly Sales")
plt.xlabel("Month")
plt.ylabel("Sales")
plt.show()

#bar graph
data2={
    "month":["January", "February", "March", "April", "May", "June"],
    "sales":[1500, 2000, 2500, 2500, 2000, 4000],
    "profit":[500, 700, 800, 1000, 1200, 1500]
}

plt.bar(data2["month"], data2["profit"])
plt.title("Monthly Profit")
plt.xlabel("Month")
plt.ylabel("Profit")
plt.show()

#pie chart
data3={
    "month":["January", "February", "March", "April", "May", "June"],
    "sales":[1500, 2000, 2500, 2500, 2000, 4000],
    "profit":[500, 700, 800, 1000, 1200, 1500]
}

plt.pie(data3["sales"], labels=data3["month"], autopct='%1.1f%%')
plt.title("Sales Distribution") 
plt.show()
