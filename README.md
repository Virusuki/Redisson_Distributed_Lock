# Redis와 Distributed Lock(분산 락)

## 테스트검증 - "분산락 미적용" without Lock
##### 시퀀스 다이어그램(Sequence Diagram)
<img src="https://github.com/Virusuki/Redisson_Distributed_Lock/blob/main/img/%EB%B6%84%EC%82%B0%EB%9D%BD%EB%AF%B8%EC%A0%81%EC%9A%A9_%EC%8B%9C%ED%80%80%EC%8A%A4%20diagram.PNG" width="700px" height="550px" title="px(픽셀) 크기 설정" alt="kubernetes mornitoring architecture"></img><br/>

##### 테스트 결과
<img src="https://github.com/Virusuki/Redisson_Distributed_Lock/blob/main/img/%EB%B6%84%EC%82%B0%EB%9D%BD%EB%AF%B8%EC%A0%81%EC%9A%A9_junit.PNG" width="900px" height="80px" title="px(픽셀) 크기 설정" alt="kubernetes mornitoring architecture"></img><br/>
<img src="https://github.com/Virusuki/Redisson_Distributed_Lock/blob/main/img/%EB%B6%84%EC%82%B0%EB%9D%BD%EB%AF%B8%EC%A0%81%EC%9A%A9.PNG" width="600px" height="550px" title="px(픽셀) 크기 설정" alt="kubernetes mornitoring architecture"></img><br/>


## 테스트검증 - "분산락 적용" with Lock
##### 시퀀스 다이어그램(Sequence Diagram)
<img src="https://github.com/Virusuki/Redisson_Distributed_Lock/blob/main/img/%EB%B6%84%EC%82%B0%EB%9D%BD%EC%A0%81%EC%9A%A9%20%EC%8B%9C%ED%80%80%EC%8A%A4%20diagram.PNG" width="600px" height="450px" title="px(픽셀) 크기 설정" alt="kubernetes mornitoring architecture"></img><br/>

##### 테스트 결과
<img src="https://github.com/Virusuki/Redisson_Distributed_Lock/blob/main/img/%EB%B6%84%EC%82%B0%EB%9D%BD%EC%A0%81%EC%9A%A9_junit.PNG" width="900px" height="80px" title="px(픽셀) 크기 설정" alt="kubernetes mornitoring architecture"></img><br/>
<img src="https://github.com/Virusuki/Redisson_Distributed_Lock/blob/main/img/%EB%B6%84%EC%82%B0%EB%9D%BD%EC%A0%81%EC%9A%A9.PNG" width="600px" height="550px" title="px(픽셀) 크기 설정" alt="kubernetes mornitoring architecture"></img><br/>


##
Publish:
- https://s-core.co.kr/insight/view/redis%eb%a5%bc-%ed%99%9c%ec%9a%a9%ed%95%9c-%ec%95%88%ec%a0%84%ed%95%98%ea%b2%8c-%eb%8f%99%ec%8b%9c%ec%84%b1-%ec%9d%b4%ec%8a%88-%ec%a0%9c%ec%96%b4%ed%95%98%ea%b8%b0/
