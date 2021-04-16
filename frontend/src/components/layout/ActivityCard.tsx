// Material UI Components
import { makeStyles } from '@material-ui/core/styles';
import { Card, CardActionArea, CardContent, CardMedia, Typography } from '@material-ui/core/';

// Project Components
import { Link } from 'react-router-dom';
import URLS from 'URLS';
import { Activity } from 'types/Types';

const useStyles = makeStyles((theme) => ({
  root: {
    maxWidth: 345,
    marginBottom: theme.spacing(1),
  },
  media: {
    height: 140,
  },
  link: {
    textDecoration: 'none',
  },
}));

export type ActivityCardProps = {
  activity: Activity;
};

export default function ActivityCard(props: ActivityCardProps) {
  const classes = useStyles();

  return (
    <Link className={classes.link} to={`${URLS.ACTIVITIES}${props.activity.activityId}/`}>
      <Card className={classes.root}>
        <CardActionArea>
          <CardMedia className={classes.media} image={props.activity.image} />
          <CardContent>
            <Typography component='h2' gutterBottom variant='h5'>
              {props.activity.title}
            </Typography>
            <Typography variant='body2'>{props.activity.description}</Typography>
          </CardContent>
        </CardActionArea>
      </Card>
    </Link>
  );
}
