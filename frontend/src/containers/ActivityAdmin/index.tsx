import { useState } from 'react';
import URLS from 'URLS';
import { useNavigate, useParams } from 'react-router-dom';
import { useActivities } from 'hooks/Activities';

// Material-UI
import { makeStyles, Theme } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';
import Collapse from '@material-ui/core/Collapse';

// Icons
import EditIcon from '@material-ui/icons/EditRounded';
import ParticipantsIcon from '@material-ui/icons/PeopleRounded';

// Project components
import Paper from 'components/layout/Paper';
import Tabs from 'components/layout/Tabs';
import Navigation from 'components/navigation/Navigation';
import SidebarList from 'components/layout/SidebarList';
import ActivityEditor from 'containers/ActivityAdmin/components/ActivityEditor';
// import ActivityParticipants from 'containers/ActivityAdmin/components/ActivityParticipants';

const useStyles = makeStyles((theme: Theme) => ({
  root: {
    padding: theme.spacing(4),
    marginLeft: theme.spacing(35),
    [theme.breakpoints.down('md')]: {
      padding: theme.spacing(4, 1, 6),
      marginLeft: 0,
    },
  },
  content: {
    maxWidth: 900,
    margin: '0 auto',
  },
  header: {
    color: theme.palette.text.primary,
    paddingLeft: theme.spacing(2),
  },
}));

const ActivityAdmin = () => {
  const classes = useStyles();
  const navigate = useNavigate();
  const { activityId } = useParams();
  const editTab = { value: 'edit', label: activityId ? 'Endre' : 'Opprett', icon: EditIcon };
  const participantsTab = { value: 'participants', label: 'Påmeldte', icon: ParticipantsIcon };
  const tabs = [editTab, participantsTab];
  const [tab, setTab] = useState(editTab.value);

  const goToActivity = (newActivity: string | null) => {
    if (newActivity) {
      navigate(`${URLS.ADMIN_ACTIVITIES}${newActivity}/`);
    } else {
      navigate(URLS.ADMIN_ACTIVITIES);
    }
  };

  return (
    <Navigation maxWidth={false} noFooter>
      <SidebarList onItemClick={(id: string | null) => goToActivity(id || null)} selectedItemId={activityId} title='Aktiviteter' useHook={useActivities} />
      <div className={classes.root}>
        <div className={classes.content}>
          <Typography className={classes.header} variant='h2'>
            {activityId ? 'Rediger aktivitet' : 'Opprett aktivitet'}
          </Typography>
          <Tabs selected={tab} setSelected={setTab} tabs={tabs} />
          <Paper>
            <Collapse in={tab === editTab.value} mountOnEnter>
              <ActivityEditor activityId={activityId} goToActivity={goToActivity} />
            </Collapse>
            <Collapse in={tab === participantsTab.value} mountOnEnter>
              {/* <ActivityParticipants activityId={activityId} /> */}
            </Collapse>
          </Paper>
        </div>
      </div>
    </Navigation>
  );
};

export default ActivityAdmin;