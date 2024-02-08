import { useEffect, useState } from 'react';
import styled from 'styled-components';
import { colors } from '../../../../config/Color';
import { Spacer } from '../../../atoms/basic/Spacer';
import { Body1, Body2 } from '../../../atoms/basic/Typography';
import IconButton from '../../../atoms/button/IconButton';
import { IoIcon } from '../../../atoms/icon/Icons';
import { difficulty } from '../../../../utils/json/difficulty';
import { getProfile } from '../../../../utils/api/mypage/myprofile/profile';
import ModalProfile from '../../../organisms/myPage/main/ModalProfile';
import { getDownloadURL, ref } from '@firebase/storage';
import { fstorage } from '../../../../firebase';

const StyledContainer = styled.div`
  background-color: ${colors.GrayBlue[200]};
  border-radius: 2vmax;
  display: flex;
  flex-direction: column;
  width: 100%;
  height: fit-content;
  padding: 2vh;
  box-sizing: border-box;
`;

const StyledBackgroundImage = styled.div`
  display: flex;
  align-items: flex-end;
  justify-content: center;
  border-radius: 1em;
  background-image: url('/images/code_odyssey/MyProfileBg.svg');
  background-repeat: no-repeat;
  background-size: cover;
  min-height: 12vh;
  background-position: center center;
`;

const StyledMyImgContainer = styled.div`
  display: flex;
  width: 4vmax;
  height: 4vmax;
  border-radius: 5em;
`;

const StyledMyImage = styled.img`
  width: 100%;
  object-fit: cover;
  margin: 0 auto;
  border-radius: 5em;
  border: 2px solid ${colors.Gray[700]};
  box-sizing: border-box;
`;

const StyledMyInfoContainer = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
`;

const StyledPositionContainer = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
`;

const StyledMyInfoContentContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-around;
`;

const DiffImgageDiv = styled.img`

  position: relative;
  width: 4%;
`;

const MyProfileDetailCard = () => {
  const [data, setData] = useState({
    thumbnail: 'https://picsum.photos/300',
    nickname: 'testNickName',
    email: 'test@test.com',
    tier: 31,
    streak: 17,
    sevenStreak: 2,
    penalty: 1,
  });


  const fetchData = async () => {
    const data= await getProfile()
    console.log(data)
    // 파이어 베이스에서 이미지 가져오는 로직 추가
    if (data.thumbnail && data.thumbnail.includes('firebase')) {
      const url = await getDownloadURL(ref(fstorage, data.thumbnail));
      setData({
        ...data,
        thumbnail : url
      })
    // 카카오에서 썸네일 가져왔다면 바로 집어넣기      
    } else {
      setData( data );
    }
  };  

  useEffect(() => {
      fetchData();
  }, [  data.thumbnail,data.nickname]);

  const [isModalOpen, setModalOpen] = useState(false);

  const openModal = () => setModalOpen(true)
  const closeModal = () => setModalOpen(false)

  return (
    <StyledContainer>
      <StyledBackgroundImage>
        <StyledMyImgContainer>
          <StyledMyImage src={data.thumbnail} />
        </StyledMyImgContainer>
      </StyledBackgroundImage>
      <Spacer space={'2vh'} />

      <StyledPositionContainer>
      <div style={{width:'calc(4% + 1vw)'}}></div>
        <StyledMyInfoContentContainer>
          <Body1 children={data.nickname} color={colors.Gray[25]} fontWeight={'bold'} />
          <Body2 children={data.email} color={colors.Gray[500]} />
        </StyledMyInfoContentContainer>
        <Spacer space={'1vw'} horizontal />
        <DiffImgageDiv src={difficulty[data.tier]}></DiffImgageDiv>
      </StyledPositionContainer>
      <Spacer space={'1vh'} />

      <StyledMyInfoContainer>
        <StyledMyInfoContentContainer>
          <Body1 children={data.streak || 0} color={colors.Gray[25]} fontWeight={'bold'} />
          <Body2 children={'모은 별'} color={colors.Gray[500]} />
        </StyledMyInfoContentContainer>
        <Spacer space={'2vw'} horizontal />
        <StyledMyInfoContentContainer>
          <Body1 children={data.sevenStreak || 0} color={colors.Gray[25]} fontWeight={'bold'} />
          <Body2 children={'모은 북두칠성'} color={colors.Gray[500]} />
        </StyledMyInfoContentContainer>
        <Spacer space={'2vw'} horizontal />
        <StyledMyInfoContentContainer>
          <Body1 children={data.penalty || 0} color={colors.Gray[25]} fontWeight={'bold'} />
          <Body2 children={'범죄 지수'} color={colors.Gray[500]} />
        </StyledMyInfoContentContainer>
        <Spacer space={'2vw'} horizontal />
        <IconButton event={openModal}>
          <IoIcon name={'brush'} size={'2vmax'} color={colors.Gray[25]} />
        </IconButton>
        <ModalProfile isOpen={isModalOpen} closeModal={closeModal} nickname ={data.nickname}/>
      </StyledMyInfoContainer>
    </StyledContainer>
  );
};

export default MyProfileDetailCard;
