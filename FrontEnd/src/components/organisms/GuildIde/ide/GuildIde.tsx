import { useEffect, useState } from 'react';
import Editor, { useMonaco } from '@monaco-editor/react';
import GitHubDark from '../../../../config/code_themes/GitHub Dark.json';
import GitHubLight from '../../../../config/code_themes/GitHub Light.json';
import { editor } from 'monaco-editor';
import SprintSelectBar from '../../../molecules/buttonBar/SprintSelectBar';
import styled from 'styled-components';
import { Spacer } from '../../../atoms/basic/Spacer';
import ProblemCompileForm from '../../../molecules/form/ProblemCompileForm';
import BasicButton from '../../../atoms/button/BasicButton';
import { colors } from '../../../../config/Color';
import { Caption1 } from '../../../atoms/basic/Typography';
import ToggleSwitch from '../../../atoms/select/ToggleSwitch';
import * as StompJs from '@stomp/stompjs';

const StyledContainer = styled.div`
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 100%;
`;

const StyledMenuContainer = styled.div`
  display: flex;
  width: 100%;
  align-items: center;
  justify-content: space-between;
`;

const GuildIde = () => {
  const urlParams = new URLSearchParams(window.location.search);
  const guild_problem_id = JSON.parse(decodeURIComponent(urlParams.get('guild_problem_id') as string));
  let [client, changeClient] = useState<StompJs.Client>(new StompJs.Client());
  const [isActive, setIsActive] = useState(true);
  const [activeLanguage, setActiveLanguage] = useState('java');
  const monaco = useMonaco();
  const [input, setInput] = useState('');
  const [selectedTheme, setSelectedTheme] = useState(true);

  const handleEditorChange = (value: string | undefined, event: editor.IModelContentChangedEvent) => {
    if (value !== undefined) {
      setInput(value);
      client.publish({
        destination: '/pub/ide/' + guild_problem_id,
        body: JSON.stringify({ code: value, guildProblemId: guild_problem_id }),
      });
    }
  };

  useEffect(() => {
    if (!monaco) return;

    monaco.editor.defineTheme('dark', GitHubDark as editor.IStandaloneThemeData);
    monaco.editor.defineTheme('light', GitHubLight as editor.IStandaloneThemeData);

    monaco.editor.setTheme(selectedTheme ? 'light' : 'dark');
  }, [monaco, selectedTheme]);

  const callback = function (message: any) {
    if (message.body) {
      let msg = JSON.parse(message.body);
      setInput(msg.code);
    }
  };

  const connect = () => {
    try {
      const clientdata = new StompJs.Client({
        brokerURL: `ws://localhost:8888/ws`,
        //   connectHeaders: { Authorization: '' },
        reconnectDelay: 5000,
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000,
      });
      clientdata.onConnect = function () {
        clientdata.subscribe(`/topic/ide.${guild_problem_id}`, callback);
      };

      clientdata.activate();
      changeClient(clientdata);
    } catch (err) {
      console.log(err);
    }
  };

  const disConnect = () => {
    if (client === null) {
      return;
    }
    client.deactivate();
  };

  useEffect(() => {
    connect();
    return () => disConnect();
  }, []);

  return (
    <StyledContainer>
      <StyledMenuContainer>
        <div style={{ width: '40%' }}>
          <SprintSelectBar
            data={[
              { content: 'java', event: () => setActiveLanguage('java'), active: activeLanguage === 'java' },
              { content: 'python', event: () => setActiveLanguage('python'), active: activeLanguage === 'python' },
              { content: 'c++', event: () => setActiveLanguage('c++'), active: activeLanguage === 'c++' },
            ]}
          />
        </div>
        <div style={{ display: 'flex', alignItems: 'center', gap: '2vmin' }}>
          <ToggleSwitch setSelectedTheme={setSelectedTheme} />
          <BasicButton
            width={'auto'}
            event={() => {
              setInput('');
            }}
            borderColor={'rgba(0, 0, 0, 0)'}
            deepColor={'rgba(255, 80, 80, 0.2)'}
            bgColor={'rgba(255, 120, 120, 0.2)'}
            children={<Caption1 children={'reset'} color={colors.White} fontWeight={'bold'} />}
            borderRadius={'2em'}
            padding={'1vmin'}
          />
        </div>
      </StyledMenuContainer>
      <Editor
        height={isActive ? '48vh' : '76vh'}
        language={activeLanguage}
        defaultValue={''}
        theme={'true'}
        value={input}
        width={'40vw'}
        options={{ minimap: { enabled: false } }}
        onChange={handleEditorChange}
      />
      <Spacer space={'1vmin'} />
      <ProblemCompileForm isActive={isActive} setIsActive={setIsActive} />
    </StyledContainer>
  );
};

export default GuildIde;