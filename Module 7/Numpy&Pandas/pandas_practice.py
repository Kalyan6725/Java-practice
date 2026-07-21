import pandas as pd

a=pd.Series([1, 2, 3, 4, 5])
print(a)

#dataframe
data = {'Name': ['Alice', 'Bob', 'Charlie', 'David'],
        'Age': [25, 30, 35, 40],
        'Email': ['alice@gmail.com', 'bob@gmail.com', 'charlie@gmail.com', 'david@gmail.com']}
df = pd.DataFrame(data)
print(df)
#df.to_csv('data.csv', index=False)

print(df[['Name','Age']])

NameAliceFilter=df[df['Name']=='Alice']
print(NameAliceFilter)

AgeFilter=df[df['Age']>30]
print(AgeFilter)