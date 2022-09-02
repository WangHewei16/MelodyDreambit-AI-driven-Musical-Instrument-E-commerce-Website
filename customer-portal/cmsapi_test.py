# pip install requests
import requests

headers = {
    "Authorization": "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJmcmVzaCI6ZmFsc2UsImlhdCI6MTYzODI0MTUyMSwianRpIjoiYTViYTYxNDktY2I2OS00NjZhLTljYTMtZDRmOTA4NTRjMjY3IiwidHlwZSI6ImFjY2VzcyIsInN1YiI6ImZpdWhxRGhLNldvNlJiOWhIYzlmZlgiLCJuYmYiOjE2MzgyNDE1MjEsImV4cCI6MTYzODI0MjQyMX0.N1PH-EKTTVL0SWDiGyzdi4PMwWniETbNMLMxHc8BV7g"
}
resp = requests.get("http://127.0.0.1:5000/cmsapi", headers=headers)
print(resp.text)