import styled from 'styled-components';
import { Spacer } from '../basic/Spacer';
import { colors } from '../../../config/Color';

interface GuildBasicButtonProps {
  event: () => void;
  src: string;
  spacer?: string | number;
}

const StyledGuildBtnContainer = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  width: 2.5vw;
  min-width: 2.5vw;
  height: 2.5vw;
  min-height: 2.5vw;
`;

const StyledButton = styled.button<{ theme: { [key: string]: string }; width: number | string; src: string }>`
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 2em;
  width: 100%;
  padding: 0;
  margin: 0;
  height: 100%;
  border-width: 0;
  transition: all 100ms ease-in-out;
  background-image: url(${(props) => props.src});
  background-repeat: no-repeat;
  background-size: contain;
  background-position: center center;
  background-color: rgba(0, 0, 0, 0);

  &:hover {
    border-radius: 1em;
    background-color: rgba(0, 0, 0, 0);
    box-shadow: 0px 0px 1em ${colors.Indigo[600]};
    cursor: pointer;
  }

  &.active {
    border-radius: 1em;
    background-color: rgba(0, 0, 0, 0);
    box-shadow: 0px 0px 1em ${colors.Indigo[600]};
    cursor: pointer;
  }
`;

const GuildButton = (props: GuildBasicButtonProps) => {
  return (
    <>
      <Spacer space={props.spacer || '2vh'} />
      <StyledGuildBtnContainer>
        <StyledButton type='button' onClick={props.event} width={'100%'} src={props.src} />
      </StyledGuildBtnContainer>
      <Spacer space={props.spacer || '2vh'} />
    </>
  );
};

export default GuildButton;
