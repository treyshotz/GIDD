import { useCallback, useState } from 'react';
import { useForm } from 'react-hook-form';
import { Activity, LatLng } from 'types/Types';
import { TrainingLevel } from 'types/Enums';
import { traningLevelToText } from 'utils';
import { GOOGLE_MAPS_API_KEY } from 'constant';

// Material UI Components
import { makeStyles } from '@material-ui/core/styles';
import MenuItem from '@material-ui/core/MenuItem';
import { Button, IconButton, InputBase, Collapse, Divider, TextField } from '@material-ui/core';

// Icons
import FilterIcon from '@material-ui/icons/TuneRounded';
import SearchIcon from '@material-ui/icons/SearchRounded';

// Project components
import { ActivityFilters } from 'containers/Activities';
import DatePicker from 'components/inputs/DatePicker';
import Select from 'components/inputs/Select';
import Paper from 'components/layout/Paper';
import SubmitButton from 'components/inputs/SubmitButton';
import Slider from 'components/inputs/Slider';

// Google Maps Components
import { GoogleMap, LoadScript, Circle, Autocomplete } from '@react-google-maps/api';

const useStyles = makeStyles((theme) => ({
  paper: {
    padding: theme.spacing(0.25, 0.5),
    overflow: 'hidden',
  },
  root: {
    display: 'flex',
    alignItems: 'center',
    width: '100%',
  },
  input: {
    marginLeft: theme.spacing(1),
    flex: 1,
  },
  iconButton: {
    padding: theme.spacing(1),
  },
  filterPaper: {
    padding: theme.spacing(1),
  },
  level: {
    display: 'flex',
    flexDirection: 'column',
  },
  grid: {
    display: 'grid',
    gap: theme.spacing(1),
  },
  mapContainerStyle: {
    width: '100%',
    height: 400,
    borderRadius: theme.shape.borderRadius,
    [theme.breakpoints.down('md')]: {
      height: 300,
    },
  },
  mapFilter: {
    marginTop: theme.spacing(1),
    marginBottom: theme.spacing(1),
  },
  mapSearch: {
    marginBottom: theme.spacing(1),
  },
}));

type FormValues = Partial<Pick<Activity, 'title'>> & {
  endDate?: Date;
  startDate?: Date;
  level?: Activity['level'] | '';
  radius?: number;
  latLng?: LatLng;
};

export type SearchBarProps = {
  updateFilters: (newFilters: ActivityFilters) => void;
};

const SearchBar = ({ updateFilters }: SearchBarProps) => {
  const classes = useStyles();
  const [open, setOpen] = useState(false);
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const [autocomplete, setAutocomplete] = useState<any>(null);
  const [location, setLocation] = useState<LatLng>({
    lat: 63.418022,
    lng: 10.402602,
  });
  const { watch, reset, register, handleSubmit, control, formState } = useForm<FormValues>();
  const radius = watch('radius');

  const submit = async (data: FormValues) => {
    setOpen(false);
    const filters: ActivityFilters = {};
    if (data.endDate) {
      filters.startDateBefore = data.endDate.toJSON();
    }
    if (data.startDate) {
      filters.startDateAfter = data.startDate.toJSON();
    }
    if (data.title) {
      filters.title = data.title;
    }
    if (data.level) {
      filters.level = data.level;
    }
    if (data.radius) {
      filters.radius = data.radius * 1000;
    }
    if (data.latLng) {
      filters.lat = data.latLng.lat;
      filters.lng = data.latLng.lng;
    }
    updateFilters(filters);
  };

  const resetFilters = async () => {
    setOpen(false);
    reset({
      title: '',
      level: '',
      endDate: undefined,
      startDate: undefined,
    });
    updateFilters({});
  };

  const { ref: titleRef, name: titleName } = register('title');

  const onPlaceChanged = () => {
    if (autocomplete && autocomplete.getPlace().geometry) {
      const latLng = autocomplete.getPlace().geometry.location.toJSON() as LatLng;
      setLocation(latLng);
    }
  };

  const onKeyPressed = useCallback((e: React.KeyboardEvent<HTMLDivElement>) => {
    e.key === 'Enter' && e.preventDefault();
  }, []);

  return (
    <Paper className={classes.paper} noPadding>
      <form onSubmit={handleSubmit(submit)}>
        <div className={classes.root}>
          <IconButton aria-label='menu' className={classes.iconButton} onClick={() => setOpen((prev) => !prev)}>
            <FilterIcon />
          </IconButton>
          <InputBase className={classes.input} inputRef={titleRef} name={titleName} placeholder='Søk etter aktivitet' />
          <IconButton className={classes.iconButton} type='submit'>
            <SearchIcon />
          </IconButton>
        </div>
        <Collapse in={open}>
          <Divider />
          <div className={classes.filterPaper}>
            <DatePicker control={control} formState={formState} fullWidth label='Start' margin='dense' name='startDate' type='date-time' />
            <DatePicker control={control} formState={formState} fullWidth label='Slutt' margin='dense' name='endDate' type='date-time' />
            <Select control={control} formState={formState} label='Trenings-nivå' name='level'>
              {Object.values(TrainingLevel).map((value, index) => (
                <MenuItem key={index} value={value}>
                  {traningLevelToText(value as TrainingLevel)}
                </MenuItem>
              ))}
            </Select>
            <div className={classes.mapFilter}>
              <LoadScript googleMapsApiKey={GOOGLE_MAPS_API_KEY} libraries={['places']}>
                <Autocomplete onLoad={(data) => setAutocomplete(data)} onPlaceChanged={onPlaceChanged}>
                  <TextField className={classes.mapSearch} fullWidth onKeyPress={onKeyPressed} placeholder='Søk etter sted...' />
                </Autocomplete>
                <Slider control={control} name='radius' />
                <GoogleMap center={location} mapContainerClassName={classes.mapContainerStyle} zoom={11}>
                  {radius && <Circle center={location} radius={radius * 1000} />}
                </GoogleMap>
              </LoadScript>
            </div>
            <div className={classes.grid}>
              <SubmitButton formState={formState}>Aktiver filtre</SubmitButton>
              <Button onClick={resetFilters} variant='outlined'>
                Nullstill filtre
              </Button>
            </div>
          </div>
        </Collapse>
      </form>
    </Paper>
  );
};

export default SearchBar;
